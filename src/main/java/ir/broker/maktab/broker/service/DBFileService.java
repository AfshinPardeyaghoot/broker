package ir.broker.maktab.broker.service;

import ir.broker.maktab.broker.model.request.DBFile;
import ir.broker.maktab.broker.model.request.Request;
import ir.broker.maktab.broker.repository.DBFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class DBFileService {

    private final DBFileRepository repository;

    @Autowired
    public DBFileService(DBFileRepository repository) {
        this.repository = repository;
    }

    public void delete(Integer dbFileId){
        repository.deleteById(dbFileId);
    }

    public void save(DBFile file) {
        repository.save(file);
    }

    public DBFile getFile(Integer fileId) {
        return repository.findDBFileById(fileId).get();
    }

    public DBFile findByRequest(Request request){
       DBFile file = null ;
       if (repository.findByRequest(request).isPresent())
           file = repository.findByRequest(request).get();
       return file ;
    }

    public DBFile convertMultipartFileToDBFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        DBFile dbFile = null;
        try {
            dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbFile;
    }

    public void deleteFileByRequest(Request request){
        repository.deleteDBFileByRequest(request);
    }
}
