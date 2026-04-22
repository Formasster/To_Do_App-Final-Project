package es.educastur.todo_app.tag.service;

import es.educastur.todo_app.tag.model.Tag;
import es.educastur.todo_app.tag.model.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    /*
        recibe una lista de tags como String:
        - Comprueba si existe y lo rescata
        - Si no existe, lo inserta y lo devuelve.
     */
    public List<Tag> saveOrGet(List<String> tags) {

        List<Tag> result = new ArrayList<>();

        tags.forEach(tag -> {
            Optional<Tag> val = tagRepository.findByText(tag);
            result.add(val.orElseGet(() -> tagRepository.save(Tag.builder().text(tag).build())));
        });

        return result;

    }

}
