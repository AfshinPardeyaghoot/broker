package ir.broker.maktab.broker.model.request;

import ir.broker.maktab.broker.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne
    private RequestSubject subject;

    @Lob
    private String description;

    @OneToMany(mappedBy = "request", fetch = FetchType.EAGER)
    private Set<DBFile> fileAttachments = new HashSet<>();

    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToOne
    private RequestAnswer answer;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

}
