package JamKom.com.PassportSystem;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Parish {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String parishName;
	
	@ManyToOne
	@JoinColumn(name = "subcounty_id")
	private SubCounty subcounty;
	
	@OneToMany(mappedBy = "parish")
	private List<Village> villages;

	public Long getParishyId() {
		return id;
	}

	public void setParishyId(Long parishyId) {
		this.id = parishyId;
	}

	public String getParishName() {
		return parishName;
	}

	public void setParishName(String parishName) {
		this.parishName = parishName;
	}

	public SubCounty getSubcounty() {
		return subcounty;
	}

	public void setSubcounty(SubCounty subcounty) {
		this.subcounty = subcounty;
	}

	public List<Village> getVillage() {
		return villages;
	}

	public void setVillage(List<Village> village) {
		this.villages = village;
	}
	
	

}
