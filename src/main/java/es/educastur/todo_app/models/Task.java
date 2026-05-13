package es.educastur.todo_app.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Lob
    private String description;

    private boolean completed;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private boolean starred;

    @Lob
    private String notes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            foreignKey = @ForeignKey(name = "fk_task_tag_task"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            inverseForeignKey = @ForeignKey(name = "fk_task_tag_tag")
    )

    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_user"))
    @JsonIgnore                               
    private User author;


    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_category"))
    private Category category;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Task task = (Task) o;
        return getId() != null && Objects.equals(getId(), task.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}



