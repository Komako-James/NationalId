package JamKom.com.PassportSystem;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Father {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String fathersName;
	private String fathersPlaceofBirth;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFathersName() {
		return fathersName;
	}
	public void setFathersName(String fathersName) {
		this.fathersName = fathersName;
	}
	public String getFathersPlaceofBirth() {
		return fathersPlaceofBirth;
	}
	public void setFathersPlaceofBirth(String fathersPlaceofBirth) {
		this.fathersPlaceofBirth = fathersPlaceofBirth;
	}
	

}
