package ir.broker.maktab.broker.service;

import ir.broker.maktab.broker.model.request.DBFile;
import ir.broker.maktab.broker.model.request.Request;
import ir.broker.maktab.broker.model.request.RequestStatus;
import ir.broker.maktab.broker.model.user.User;
import ir.broker.maktab.broker.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@Service
public class RequestService {


    private final RequestRepository requestRepository;

    private final DBFileService dbFileService;

    private final RequestSubjectService requestSubjectService;

    @Autowired
    public RequestService(RequestRepository requestRepository, DBFileService dbFileService, RequestSubjectService requestSubjectService) {
        this.requestRepository = requestRepository;
        this.dbFileService = dbFileService;
        this.requestSubjectService = requestSubjectService;
    }

    public void save(MultipartFile[] files, Integer subjectId, String description, User user) {


        Request request = new Request();
        request.setSubject(requestSubjectService.getRequestSubjectById(subjectId));
        request.setDescription(description);
        request.setDate(new Date());
        request.setRequestStatus(RequestStatus.ATTENDING);
        request.setUser(user);
        requestRepository.save(request);
        for (MultipartFile file : files) {
            DBFile dbFile = dbFileService.convertMultipartFileToDBFile(file);
            dbFile.setRequest(request);
            dbFileService.save(dbFile);
            request.getFileAttachments().add(dbFile);
        }

    }

    public List<Request> getRequestsByUser(User user) {
        return requestRepository.getRequestsByUser(user);
    }

    public Request getRequestById(Integer id) {
        return requestRepository.getRequestsById(id).get();
    }

    public List<Request> getAllRequests(){
        return requestRepository.findAll() ;
    }

    public List<Request> getByDateAndRequestStatus(Date date, RequestStatus requestStatus){
        return requestRepository.getRequestsByDateAndRequestStatus(date,requestStatus);
    }

    public List<Request> getByDate(Date date){
        return requestRepository.getRequestsByDate(date);
    }

    public List<Request> getByRequestStatus( RequestStatus requestStatus){
        return requestRepository.getRequestsByRequestStatus(requestStatus);
    }
}
