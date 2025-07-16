package JamKom.com.PassportSystem;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
@RestController
public class PassortController {
	
	@Autowired
	PassportService passportService;
	
	@Autowired
	DistrictRepository districtRepository;
	
	@GetMapping
	public List<District> getDistrict(){
		
		return passportService.getDistrict();
	}
	
	@GetMapping("index")
	String getIndex() {
		return "index";
	}
	
	
	private final String xmlBackupDirectory;

    public PassortController(PassportService passportService, 
                               DistrictRepository districtRepository) {
        this.passportService = passportService;
        this.districtRepository = districtRepository;
        this.xmlBackupDirectory = "./xml_backups/"; // Should be from @Value in real app
    }

    @GetMapping
    public String showApplicationForm(Model model) {
        // Add empty models for form binding
        model.addAttribute("applicant", new PassportModel());
        model.addAttribute("mother", new Mother());
        model.addAttribute("father", new Father());
        
        // Load districts for dropdowns
        List<District> districts = passportService.getDistrict();
        model.addAttribute("districts", districts);
        
        return "application";
    }

    @PostMapping("/submit")
    public String submitApplication(
            // Applicant fields
            @RequestParam String surname,
            @RequestParam String givenName,
            @RequestParam String profession,
            @RequestParam String nin,
            @RequestParam String cardNo,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String otherNumber,
            @RequestParam String gender,
            @RequestParam String dob,
            @RequestParam String maritalStatus,
            @RequestParam(required = false) String email,
            @RequestParam String citizenshipType,
            
            // Residential address
            @RequestParam Long resDistrict,
            @RequestParam Long resSubcounty,
            @RequestParam Long resParish,
            @RequestParam Long resVillage,
            
            // Origin details
            @RequestParam Long originDistrict,
            @RequestParam Long originSubcounty,
            @RequestParam Long originParish,
            @RequestParam Long originVillage,
            
            // Mother's info
            @RequestParam String mothersName,
            @RequestParam Long motherDistrict,
            @RequestParam Long motherSubcounty,
            @RequestParam Long motherParish,
            @RequestParam Long motherVillage,
            
            // Father's info
            @RequestParam String fathersName,
            @RequestParam Long fatherDistrict,
            @RequestParam Long fatherSubcounty,
            @RequestParam Long fatherParish,
            @RequestParam Long fatherVillage,
            
            // Biometrics
            @RequestParam MultipartFile applicantPhoto,
            @RequestParam MultipartFile fingerprint,
            
            // Documents
            @RequestParam MultipartFile document1,
            @RequestParam(required = false) MultipartFile document2,
            Model model) {
        
        try {
            // 1. Save uploaded files
            String uploadDir = "./uploads/";
            Files.createDirectories(Paths.get(uploadDir));
            
            String photoPath = saveFile(applicantPhoto, uploadDir);
            String fingerprintPath = saveFile(fingerprint, uploadDir);
            String doc1Path = saveFile(document1, uploadDir);
            String doc2Path = document2 != null ? saveFile(document2, uploadDir) : null;
            
            // 2. Create and save application entities
            // Create Mother entity
            Mother mother = new Mother();
            mother.setMothersName(mothersName);
            mother.setPlaceOfBirth(formatAddress(motherDistrict, motherSubcounty, motherParish, motherVillage));
            
            // Create Father entity
            Father father = new Father();
            father.setFathersName(fathersName);
            father.setFathersPlaceofBirth(formatAddress(fatherDistrict, fatherSubcounty, fatherParish, fatherVillage));
            
            // Create Biometrics entity
            Biometrics biometrics = new Biometrics();
            biometrics.setImage(Files.readAllBytes(Paths.get(photoPath)));
            biometrics.setFingerprints(Files.readAllBytes(Paths.get(fingerprintPath)));
            
            // Create PassportModel entity
            PassportModel applicant = new PassportModel();
            applicant.setSurname(surname);
            applicant.setGivenName(givenName);
            applicant.setProfession(profession);
            applicant.setNin(nin);
            applicant.setCardNumber(cardNo);
            applicant.setPhoneNumber(phoneNumber);
            applicant.setOtherNumber(otherNumber);
            applicant.setGender(gender);
            applicant.setDateOfBirth(parseDate(dob));
            applicant.setMaritalStatus(maritalStatus);
            applicant.setEmail(email);
            
            
            // Set residence
            HoldersResidence residence = new HoldersResidence();
            residence.setDistrict(districtRepository.findById(resDistrict).orElse(null));
            // Note: In real implementation, you'd need to set subcounty, parish, village similarly
            applicant.setResidenceId(residence);
            
            // Set relationships
            applicant.setMothersId(mother);
            applicant.setFathersId(father);
            applicant.setBiometrics(biometrics);
            
            // Save to database (implementation would be in your service)
            passportService.saveApplication(applicant, mother, father, biometrics);
            
            // 3. Generate XML
            String xmlFilePath = generateApplicationXml(
                applicant, mother, father,
                resDistrict, resSubcounty, resParish, resVillage,
                originDistrict, originSubcounty, originParish, originVillage,
                photoPath, fingerprintPath, doc1Path, doc2Path
            );
            
            model.addAttribute("message", "Application submitted successfully!");
            model.addAttribute("xmlFilePath", xmlFilePath);
            return "application-success";
        } catch (Exception e) {
            model.addAttribute("error", "Error submitting application: " + e.getMessage());
            return showApplicationForm(model);
        }
    }

    private String formatAddress(Long districtId, Long subcountyId, Long parishId, Long villageId) {
        // In a real implementation, you would fetch these names from the database
        return String.format("District: %d, Subcounty: %d, Parish: %d, Village: %d", 
                districtId, subcountyId, parishId, villageId);
    }

    private Date parseDate(String dob) throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dob);
    }

    private String saveFile(MultipartFile file, String directory) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(directory + fileName);
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }

    private String generateApplicationXml(
            PassportModel applicant, Mother mother, Father father,
            Long resDistrict, Long resSubcounty, Long resParish, Long resVillage,
            Long originDistrict, Long originSubcounty, Long originParish, Long originVillage,
            String photoPath, String fingerprintPath, String doc1Path, String doc2Path) throws Exception {
        
        // Create XML document
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        
        // Root element
        Element rootElement = doc.createElement("PassportApplication");
        doc.appendChild(rootElement);
        
        // Application date
        Element appDate = doc.createElement("ApplicationDate");
        appDate.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        rootElement.appendChild(appDate);
        
        // Applicant information
        Element applicantInfo = doc.createElement("Applicant");
        addTextElement(doc, applicantInfo, "Surname", applicant.getSurname());
        addTextElement(doc, applicantInfo, "GivenName", applicant.getGivenName());
        addTextElement(doc, applicantInfo, "Profession", applicant.getProfession());
        addTextElement(doc, applicantInfo, "NIN", applicant.getNin());
        addTextElement(doc, applicantInfo, "CardNumber", applicant.getCardNumber());
        addTextElement(doc, applicantInfo, "PhoneNumber", applicant.getPhoneNumber());
        addTextElement(doc, applicantInfo, "OtherNumber", applicant.getOtherNumber());
        addTextElement(doc, applicantInfo, "Gender", applicant.getGender());
        addTextElement(doc, applicantInfo, "DateOfBirth", 
                      new SimpleDateFormat("yyyy-MM-dd").format(applicant.getDateOfBirth()));
        addTextElement(doc, applicantInfo, "MaritalStatus", applicant.getMaritalStatus());
        addTextElement(doc, applicantInfo, "Email", applicant.getEmail());
        rootElement.appendChild(applicantInfo);
        
        // Residential Address
        Element resAddress = doc.createElement("ResidentialAddress");
        addAddressElements(doc, resAddress, resDistrict, resSubcounty, resParish, resVillage);
        rootElement.appendChild(resAddress);
        
        // Origin Details
        Element originDetails = doc.createElement("OriginDetails");
        addAddressElements(doc, originDetails, originDistrict, originSubcounty, originParish, originVillage);
        rootElement.appendChild(originDetails);
        
        // Mother's Information
        Element motherInfo = doc.createElement("Mother");
        addTextElement(doc, motherInfo, "Name", mother.getMothersName());
        Element motherAddress = doc.createElement("Address");
        // Parse the formatted address back to components if needed
        addTextElement(doc, motherAddress, "Details", mother.getPlaceOfBirth());
        motherInfo.appendChild(motherAddress);
        rootElement.appendChild(motherInfo);
        
        // Father's Information
        Element fatherInfo = doc.createElement("Father");
        addTextElement(doc, fatherInfo, "Name", father.getFathersName());
        Element fatherAddress = doc.createElement("Address");
        addTextElement(doc, fatherAddress, "Details", father.getFathersPlaceofBirth());
        fatherInfo.appendChild(fatherAddress);
        rootElement.appendChild(fatherInfo);
        
        // Biometrics
        Element biometrics = doc.createElement("Biometrics");
        addTextElement(doc, biometrics, "PhotoPath", photoPath);
        addTextElement(doc, biometrics, "FingerprintPath", fingerprintPath);
        rootElement.appendChild(biometrics);
        
        // Documents
        Element documents = doc.createElement("Documents");
        addTextElement(doc, documents, "Document1", doc1Path);
        if (doc2Path != null) {
            addTextElement(doc, documents, "Document2", doc2Path);
        }
        rootElement.appendChild(documents);
        
        // Write to file
        Files.createDirectories(Paths.get(xmlBackupDirectory));
        String xmlFileName = xmlBackupDirectory + "passport_app_" + applicant.getNin() + "_" + 
                           System.currentTimeMillis() + ".xml";
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(xmlFileName));
        
        transformer.transform(source, result);
        
        return xmlFileName;
    }

    private void addAddressElements(Document doc, Element parent, 
                                  Long district, Long subcounty, Long parish, Long village) {
        addTextElement(doc, parent, "District", district.toString());
        addTextElement(doc, parent, "Subcounty", subcounty.toString());
        addTextElement(doc, parent, "Parish", parish.toString());
        addTextElement(doc, parent, "Village", village.toString());
    }

    private void addTextElement(Document doc, Element parent, String name, String value) {
        if (value != null) {
            Element element = doc.createElement(name);
            element.appendChild(doc.createTextNode(value));
            parent.appendChild(element);
        }
    }

    // AJAX endpoint for cascading dropdowns
    @GetMapping("/subcounties")
    @ResponseBody
    public List<SubCounty> getSubcountiesByDistrict(@RequestParam Long districtId) {
        return passportService.getSubcountiesByDistrict(districtId);
    }

    @GetMapping("/parishes")
    @ResponseBody
    public List<Parish> getParishesBySubcounty(@RequestParam Long subcountyId) {
        return passportService.getParishesBySubcounty(subcountyId);
    }

    @GetMapping("/villages")
    @ResponseBody
    public List<Village> getVillagesByParish(@RequestParam Long parishId) {
        return passportService.getVillagesByParish(parishId);
    }
}




