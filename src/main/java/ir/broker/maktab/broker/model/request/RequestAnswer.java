package ir.broker.maktab.broker.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RequestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status;

    private String description;

    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToOne
    private Request request;

}
