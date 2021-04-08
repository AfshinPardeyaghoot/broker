package ir.broker.maktab.broker.service;

import ir.broker.maktab.broker.model.request.RequestSubject;
import ir.broker.maktab.broker.repository.RequestSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Optional;

@Service
public class RequestSubjectService {

    private final RequestSubjectRepository repository;

    @Autowired
    public RequestSubjectService(RequestSubjectRepository repository) {
        this.repository = repository;
    }

    public List<RequestSubject> getAllRequestSubjects() {
        return repository.findAll();
    }

    public RequestSubject getRequestSubjectById(Integer id) {
        Optional<RequestSubject> requestSubject = repository.findById(id);
        if (requestSubject.isPresent()) {
            return requestSubject.get();
        } else {
            return null;
        }
    }

    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    public void save(RequestSubject subject){
        repository.save(subject);
    }

}
