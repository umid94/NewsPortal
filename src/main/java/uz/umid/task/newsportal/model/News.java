package uz.umid.task.newsportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Zagalovok ne mojet byt pustym")
    @Size(min = 5, message = "Ne menee 10 simvolov")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Kontent novostya ne mojet byt pustym")
    @Size(min = 50, max = 500, message = "ne bolee 500 simvolov")
    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "approved_date")
    private Date approvedDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name="author_id", nullable=false)
    private User user;

}
