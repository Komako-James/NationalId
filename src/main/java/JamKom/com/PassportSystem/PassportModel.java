package JamKom.com.PassportSystem;





import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Personal_Information")
public class PassportModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	private String Surname;
	private String givenName;
	private String phoneNumber;
	private String otherNumber;
	private String nin;
	private String cardNumber;
	private String gender;
	private String countryOfBirth;
	private Date dateOfBirth;
	private String maritalStatus;
	private String placeOfBirth;
	private String profession;
	private String email;
	
//	@Embedded
//	private HoldersResidence residence;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "residence_id", referencedColumnName = "id")
	private HoldersResidence residenceId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mothers_id", referencedColumnName = "id")
	private Mother mothersId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fathers_id", referencedColumnName = "id")
	private Father fathersId;
	
	@OneToOne(cascade= CascadeType.ALL)
	@JoinColumn(name = "biometricid", referencedColumnName = "id")
	private Biometrics biometrics;
	

	
	

	public HoldersResidence getResidenceId() {
		return residenceId;
	}
	public void setResidenceId(HoldersResidence residenceId) {
		this.residenceId = residenceId;
	}
	public Mother getMothersId() {
		return mothersId;
	}
	public void setMothersId(Mother mothersId) {
		this.mothersId = mothersId;
	}
	public Father getFathersId() {
		return fathersId;
	}
	public void setFathersId(Father fathersId) {
		this.fathersId = fathersId;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getOtherNumber() {
		return otherNumber;
	}
	public void setOtherNumber(String otherNumber) {
		this.otherNumber = otherNumber;
	}



	public Biometrics getBiometrics() {
		return biometrics;
	}
	public void setBiometrics(Biometrics biometrics) {
		this.biometrics = biometrics;
	}
//	public HoldersResidence getResidence() {
//		return residence;
//	}
//	public void setResidence(HoldersResidence residence) {
//		this.residence = residence;
//	}
	public String getNin() {
		return nin;
	}
	public void setNin(String nin) {
		this.nin = nin;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCountryOfBirth() {
		return countryOfBirth;
	}
	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getPlaceOfBirth() {
		return placeOfBirth;
	}
	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSurname() {
		return Surname;
	}
	public void setSurname(String surname) {
		Surname = surname;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	
}
