package main.java.com.carrecognizer;

import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.translate.Batchifier;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.training.util.ProgressBar;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.MalformedModelException;

import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
public class PredictionController {

    private ZooModel<Image, Classifications> model;

    private final List<String> classNames = Arrays.asList(
        "Audi", "BMW", "Ford", "Mercedes", "Opel", "Peugeot", "Renault", "Skoda", "Volkswagen"
    );

    @PostConstruct
    public void init() throws IOException, ModelNotFoundException, MalformedModelException {
        Criteria<Image, Classifications> criteria = Criteria.builder()
                .setTypes(Image.class, Classifications.class)
                // Docker-kompatibler Pfad: nur "model/..."
                .optModelPath(Paths.get("model/car_brand_saved_model"))
                .optTranslator(new MyTranslator())
                .optEngine("TensorFlow")
                .optProgress(new ProgressBar())
                .build();

        model = ModelZoo.loadModel(criteria);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/predict", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String predict(@RequestParam("file") MultipartFile file, ModelMap map) {

        Image img;
        try {
            img = ImageFactory.getInstance().fromInputStream(file.getInputStream());
        } catch (Exception e) {
            map.addAttribute("result", "Fehler: Die Datei konnte nicht als Bild gelesen werden.");
            return "index";
        }

        try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
            Classifications result = predictor.predict(img);
            map.addAttribute("result", result.topK(3));
        } catch (TranslateException e) {
            map.addAttribute("result", "Fehler beim Vorhersagen: " + e.getMessage());
        }

        return "index";
    }

    private class MyTranslator implements Translator<Image, Classifications> {

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            input = input.resize(180, 180, false);

            return new NDList(
                input.toNDArray(ctx.getNDManager())
                     .toType(DataType.FLOAT32, false)
                     .div(255f)
            );
        }

        @Override
        public Classifications processOutput(TranslatorContext ctx, NDList list) {
            return new Classifications(classNames, list.singletonOrThrow());
        }

        @Override
        public Batchifier getBatchifier() {
            return Batchifier.STACK;
        }
    }
}
