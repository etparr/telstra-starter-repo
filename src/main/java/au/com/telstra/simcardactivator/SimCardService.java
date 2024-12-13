package au.com.telstra.simcardactivator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SimCardService {

    @Autowired
    private SimCardRepository repository;

    public void saveTransaction(String iccid, String customerEmail, boolean active) {
        SimCardTransaction transaction = new SimCardTransaction(iccid, customerEmail, active);
        repository.save(transaction);
    }
    public SimCardTransaction getTransactionById(long id) {
        return repository.findById(id).orElse(null);
    }
}
