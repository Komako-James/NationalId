package JamKom.com.PassportSystem;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassportService {
	
	@Autowired
	PassportRepository passportRepository;
	
	@Autowired
	DistrictRepository districtRepository;
	
	
  
    @Autowired
    private SubCountyRepository subCountyRepository;
    
    @Autowired
    private ParishRepository parishRepository;
    
    @Autowired
    private VillageRepository villageRepository;
    
    @Autowired
    private MotherRepository motherRepository;
    
    @Autowired
    private FatherRepository fatherRepository;
    
    @Autowired
    private BiometricsRepository biometricsRepository;
	


    public List<District> getDistrict() {
        return districtRepository.findAll();
    }
    
    public List<SubCounty> getSubcountiesByDistrict(Long districtId) {
        return subCountyRepository.findByDistrictId(districtId);
    }
    
    public List<Parish> getParishesBySubcounty(Long subcountyId) {
        return parishRepository.findBySubcountyId(subcountyId);
    }
    
    public List<Village> getVillagesByParish(Long parishId) {
        return villageRepository.findByParishId(parishId);
    }
    
    @Transactional
    public void saveApplication(PassportModel applicant, Mother mother, Father father, Biometrics biometrics) {
        // Save parents first
        motherRepository.save(mother);
        fatherRepository.save(father);
        
        // Save biometrics
        biometricsRepository.save(biometrics);
        
        // Set relationships
        applicant.setMothersId(mother);
        applicant.setFathersId(father);
        applicant.setBiometrics(biometrics);
        
        // Save applicant
        passportRepository.save(applicant);
    }
    
    // Duplicate detection method
    public List<PassportModel> checkForDuplicates(String surname, String givenName, Date dob, byte[] fingerprint) {
        // This would be a more sophisticated query in a real application
        return passportRepository.findBySurnameAndGivenNameAndDateOfBirth(surname, givenName, dob);
        
        // Note: For fingerprint matching, you would need specialized libraries
        // This is just a basic text-based duplicate check
    }

}
