package JamKom.com.PassportSystem;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "districts")
public class District {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	

	String districtName;
	
	@OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
	private List<SubCounty> subcounty;

	public long getDistrictId() {
		return id;
	}

	public void setDistrictId(long districtId) {
		this.id = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public List<SubCounty> getSubcounty() {
		return subcounty;
	}

	public void setSubcounty(List<SubCounty> subcounty) {
		this.subcounty = subcounty;
	}
	
	

}
