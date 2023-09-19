package com.myth.entity.composite;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.myth.entity.Being;


public class BeingComposite {
		
	private int beingPK;
	@NotNull
	@Size(max = 20, min = 1)
	private String beingName;
	private int beingSpeciesPK;
	@NotNull
	@Size(max = 250, min = 1)
	private String beingDescription;
	private int beingGenderPK;
	@Lob
    private byte[] beingArt;
	private int beingFactionPK;
	
	
	private String beingSpecies;
	private String beingGender;
	private String beingFaction;
	
	@NotNull
	private List<String> beingAbilities;
	@NotNull
	private List<String> beingWeaknesses;
	@NotNull
	private List<String> beingSymbolism;
	@NotNull
	private List<String> beingStories;
	@NotNull
	private List<String> beingLocations;
	
	
	public BeingComposite() {
		
	}
	
	public BeingComposite(int beingPK, String beingName, int beingSpeciesPK, String beingSpecies,
			String beingDescription, int beingGenderPK, String beingGender, byte[] beingArt, int beingFactionPK,
			String beingFaction, List<String> beingAbilities, List<String> beingWeaknesses,
			List<String> beingSymbolism, List<String> beingStories, List<String> beingLocations) {
		super();
		this.beingPK = beingPK;
		this.beingName = beingName;
		this.beingSpeciesPK = beingSpeciesPK;
		this.beingSpecies = beingSpecies;
		this.beingDescription = beingDescription;
		this.beingGenderPK = beingGenderPK;
		this.beingGender = beingGender;
		this.beingArt = beingArt;
		this.beingFactionPK = beingFactionPK;
		this.beingFaction = beingFaction;
		this.beingAbilities = beingAbilities;
		this.beingWeaknesses = beingWeaknesses;
		this.beingSymbolism = beingSymbolism;
		this.beingStories = beingStories;
		this.beingLocations = beingLocations;
	}
	
	public BeingComposite(Being being, String species, String gender, String faction, List<String> abilities, List<String> weaknesses, List<String> symbols, List<String> stories, List<String> locations) {
		
		this.beingPK = being.getBeingPK();
		this.beingName = being.getBeingName();
		this.beingSpeciesPK = being.getBeingSpecies();
		this.beingDescription = being.getBeingDescription();
		this.beingGenderPK = being.getBeingGender();
		this.beingArt = being.getBeingArt();
		this.beingFactionPK = being.getBeingFaction();
		
		this.beingSpecies = species;
		this.beingGender = gender;
		this.beingFaction = faction;
		this.beingAbilities = abilities;
		this.beingWeaknesses = weaknesses;
		this.beingSymbolism = symbols;
		this.beingStories = stories;
		this.beingLocations = locations;	

	}

	public int getBeingPK() {
		return beingPK;
	}
	public void setBeingPK(int beingPK) {
		this.beingPK = beingPK;
	}
	public String getBeingName() {
		return beingName;
	}
	public void setBeingName(String beingName) {
		this.beingName = beingName;
	}
	public int getBeingSpeciesPK() {
		return beingSpeciesPK;
	}
	public void setBeingSpeciesPK(int beingSpeciesPK) {
		this.beingSpeciesPK = beingSpeciesPK;
	}
	public String getBeingSpecies() {
		return beingSpecies;
	}
	public void setBeingSpecies(String beingSpecies) {
		this.beingSpecies = beingSpecies;
	}
	public String getBeingDescription() {
		return beingDescription;
	}
	public void setBeingDescription(String beingDescription) {
		this.beingDescription = beingDescription;
	}
	public int getBeingGenderPK() {
		return beingGenderPK;
	}
	public void setBeingGenderPK(int beingGenderPK) {
		this.beingGenderPK = beingGenderPK;
	}
	public String getBeingGender() {
		return beingGender;
	}
	public void setBeingGender(String beingGender) {
		this.beingGender = beingGender;
	}
	public byte[] getBeingArt() {
		return beingArt;
	}
	public void setBeingArt(byte[] beingArt) {
		this.beingArt = beingArt;
	}
	public int getBeingFactionPK() {
		return beingFactionPK;
	}
	public void setBeingFactionPK(int beingFactionPK) {
		this.beingFactionPK = beingFactionPK;
	}
	public String getBeingFaction() {
		return beingFaction;
	}
	public void setBeingFaction(String beingFaction) {
		this.beingFaction = beingFaction;
	}
	public List<String> getBeingAbilities() {
		return beingAbilities;
	}
	public void setBeingAbilities(List<String> beingAbilities) {
		this.beingAbilities = beingAbilities;
	}
	public List<String> getBeingWeaknesses() {
		return beingWeaknesses;
	}
	public void setBeingWeaknesses(List<String> beingWeaknesses) {
		this.beingWeaknesses = beingWeaknesses;
	}
	public List<String> getBeingSymbolism() {
		return beingSymbolism;
	}
	public void setBeingSymbolism(List<String> beingSymbolism) {
		this.beingSymbolism = beingSymbolism;
	}
	public List<String> getBeingStories() {
		return beingStories;
	}
	public void setBeingStories(List<String> beingStories) {
		this.beingStories = beingStories;
	}
	public List<String> getBeingLocations() {
		return beingLocations;
	}
	public void setBeingLocations(List<String> beingLocations) {
		this.beingLocations = beingLocations;
	}

	@Override
	public String toString() {
		return "BeingComposite [beingPK=" + beingPK + ", beingName=" + beingName + ", beingSpeciesPK=" + beingSpeciesPK
				+ ", beingDescription=" + beingDescription + ", beingGenderPK=" + beingGenderPK + ", beingArt="
				+ Arrays.toString(beingArt) + ", beingFactionPK=" + beingFactionPK + ", beingSpecies=" + beingSpecies
				+ ", beingGender=" + beingGender + ", beingFaction=" + beingFaction + ", beingAbilities="
				+ beingAbilities + ", beingWeaknesses=" + beingWeaknesses + ", beingSymbolism=" + beingSymbolism
				+ ", beingStories=" + beingStories + ", beingLocations=" + beingLocations + "]";
	}
	
	
	
	

}
