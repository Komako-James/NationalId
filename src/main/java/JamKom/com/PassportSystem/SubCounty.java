package JamKom.com.PassportSystem;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class SubCounty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String subCountyName;
	
	@ManyToOne
	@JoinColumn(name = "district_id")
	private District district;
	
	@OneToMany(mappedBy = "subcounty", cascade = CascadeType.ALL)
	private List<Parish> parish;

	public Long getSubCountyId() {
		return id;
	}

	public void setSubCountyId(Long subCountyId) {
		this.id = subCountyId;
	}

	public String getSubCountyName() {
		return subCountyName;
	}

	public void setSubCountyName(String subCountyName) {
		this.subCountyName = subCountyName;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public List<Parish> getParish() {
		return parish;
	}

	public void setParish(List<Parish> parish) {
		this.parish = parish;
	}
	
	
	

}
