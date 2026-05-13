package es.educastur.todo_app.services;

import es.educastur.todo_app.models.Tag;
import es.educastur.todo_app.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import es.educastur.todo_app.models.User;

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

    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado con ID: " + id));
    }

    // Añadir al TagService
    public List<Tag> findAllByUser(User user) {
        return tagRepository.findAll(); // Cambia esto si tus Tags tienen el campo 'author'
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag update(Long id, Tag tagDetails) {
        Tag existing = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado"));
        existing.setColor(tagDetails.getColor());
        return tagRepository.save(existing);
    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

}