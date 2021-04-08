package ir.broker.maktab.broker.service;

import ir.broker.maktab.broker.model.request.Request;
import ir.broker.maktab.broker.model.request.RequestAnswer;
import ir.broker.maktab.broker.repository.RequestAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestAnswerService {

    private RequestAnswerRepository repository ;

    @Autowired
    public RequestAnswerService(RequestAnswerRepository repository) {
        this.repository = repository;
    }

    public void save(RequestAnswer answer){
        repository.save(answer);
    }

    public RequestAnswer getRequestAnswerByRequest(Request request){
        return repository.getByRequest(request).get();
    }

    public boolean existsByRequest(Request request){
        return repository.existsByRequest(request);
    }
}
