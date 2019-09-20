/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.datamigration.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.api.CohortService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import com.google.common.collect.Sets;
import java.util.Arrays;
import org.joda.time.Interval;

/**
 * @author The Bright
 */
public class CohortMaster {
	
	public final static int STARTED_ART_LAST_6MONTHS_COHORT = 1;
	
	public final static int DOCUMENTED_EDUCATIONAL_STATUS_COHORT = 2;
	
	public final static int DOCUMENTED_MARITAL_STATUS_COHORT = 3;
	
	public final static int EVER_ENROLLED_IN_CARE_COHORT = 4;
	
	public final static int ACTIVE_COHORT = 5;
	
	public final static int CLINIC_VISIT_LAST_6MONTHS_COHORT = 6;
	
	public final static int DOCUMENTED_OCCUPATIONAL_STATUS_COHORT = 7;
	
	public final static int DOCUMENTED_DIAGNOSIS_STATUS_COHORT = 8;
	
	public final static int DOCUMENTED_SEX_COHORT = 9;
	
	public final static int DOCUMENTED_ADDRESS_COHORT = 10;
	
	public final static int ACTIVE_DOCUMENTED_EDUCATIONAL_STATUS_COHORT = 11;
	
	public final static int ACTIVE_DOCUMENTED_MARITAL_STATUS_COHORT = 12;
	
	public final static int ACTIVE_DOCUMENTED_OCCUPATIONAL_STATUS_COHORT = 13;
	
	public final static int STARTED_ART_LAST_6MONTHS_DOCUMENTED_DOB = 14;
	
	public final static int DOCUMENTED_DOB_COHORT = 15;
	
	public final static int STARTED_ART_LAST_6MONTHS_DOCUMENTED_SEX = 16;
	
	public final static int STARTED_ART_LAST_6MONTHS_DOCUMENTED_DATECONFIRMED_POSITIVE = 17;
	
	public final static int STARTED_ART_LAST_6MONTHS_DOCUMENTED_HIVENROLLMENT = 18;
	
	/*
	   Concept IDs
	 */
	private final static int EDUCATIONAL_STATUS_CONCEPT = 1712;
	
	private final static int MARITAL_STATUS_CONCEPT = 1054;
	
	private final static int OCCUPATIONAL_STATUS_CONCEPT = 1542;
	
	private final static int ART_START_DATE_CONCEPT = 159599;
	
	private final static int DATE_CONFIRMED_POSITIVE = 160554;
	
	private Map<Integer, String> indicatorNamesMap = new HashMap<Integer, String>();
	
	private Map<Integer, Set<Integer>> cohortDictionary = new HashMap<Integer, Set<Integer>>();
	
	public CohortMaster() {
		loadCohortDictionary();
		loadIndicatorNamesDictionary();
	}
	
	public Set<Integer> buildCohortByConceptID(int conceptID) {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		EncounterService encounterService = Context.getEncounterService();
		List<Obs> obsList = null;
		List<Patient> patientList = null;
		List<Encounter> encounterList = null;
		patientList = patientService.getAllPatients();
		for (Patient pts : patientList) {
			encounterList = encounterService.getEncountersByPatient(pts);
			for (Encounter enc : encounterList) {
				obsList = new ArrayList<Obs>(enc.getAllObs());
				for (Obs obs : obsList) {
					if (obs.getConcept().getConceptId() == conceptID) {
						patientSet.add(obs.getPersonId());
					}
				}
			}
		}
		
		return patientSet;
	}
	
	public Set<Integer> buildCohortByConceptID(int conceptID, Date startDate, Date endDate) {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		EncounterService encounterService = Context.getEncounterService();
		List<Obs> obsList = null;
		List<Patient> patientList = null;
		List<Encounter> encounterList = null;
		patientList = patientService.getAllPatients();
		for (Patient pts : patientList) {
			encounterList = encounterService.getEncountersByPatient(pts);
			for (Encounter enc : encounterList) {
				if (isBetweenDate(startDate, endDate, enc.getEncounterDatetime())) {
					obsList = new ArrayList<Obs>(enc.getAllObs());
					for (Obs obs : obsList) {
						if (obs.getConcept().getConceptId() == conceptID) {
							patientSet.add(obs.getPersonId());
						}
					}
				}
				
			}
		}
		
		return patientSet;
		
	}
	
	public Set<Integer> buildCohortByEncounter(Date startDate, Date endDate) {
		Set<Integer> patientSet = new HashSet<Integer>();
		List<Encounter> encounterList = new ArrayList<Encounter>();
		EncounterService encounterService = Context.getEncounterService();
		PatientService patientService = Context.getPatientService();
		DateTime startDateTime, endDateTime;
		endDateTime = new DateTime(endDate);
		startDateTime = new DateTime(startDate);
		
		List<Patient> patientList = patientService.getAllPatients();
		for (Patient pts : patientList) {
			encounterList = encounterService.getEncountersByPatient(pts);
			for (Encounter enc : encounterList) {
				//encounterDateTime = new DateTime(enc.getEncounterDatetime());
				if (isBetweenDate(startDateTime.toDate(), endDateTime.toDate(), enc.getEncounterDatetime())) {
					patientSet.add(enc.getPatient().getPatientId());
				}
			}
		}
		
		for (Encounter enc : encounterList) {
			patientSet.add(enc.getPatient().getPatientId());
		}
		return patientSet;
	}
	
	public Set<Integer> buildCohortByEncounterInLast(int numberOfMonths) {
		Set<Integer> patientSet = new HashSet<Integer>();
		EncounterService encounterService = Context.getEncounterService();
		PatientService patientService = Context.getPatientService();
		Date startDate = null, endDate = new Date();
		DateTime startDateTime, endDateTime;
		endDateTime = new DateTime(endDate);
		startDateTime = endDateTime.minusMonths(numberOfMonths);
		List<Encounter> encounterList = new ArrayList<Encounter>();
		List<Patient> patientList = patientService.getAllPatients();
		for (Patient pts : patientList) {
			encounterList = encounterService.getEncountersByPatient(pts);
			for (Encounter enc : encounterList) {
				//encounterDateTime = new DateTime(enc.getEncounterDatetime());
				if (isBetweenDate(startDateTime.toDate(), endDateTime.toDate(), enc.getEncounterDatetime())) {
					patientSet.add(enc.getPatient().getPatientId());
				}
			}
		}
		
		return patientSet;
		
	}
	
	public Set<Integer> buildCohortByHIVEnrollment() {
		Set<Integer> patientSet = new HashSet<Integer>();
		EncounterService encounterService = Context.getEncounterService();
		PatientService patientService = Context.getPatientService();
		List<Patient> patientList = patientService.getAllPatients();
		List<Encounter> encounterList = null;
		for (Patient patient : patientList) {
			encounterList = encounterService.getEncountersByPatient(patient);
			for (Encounter enc : encounterList) {
				if (enc != null && enc.getForm() != null && enc.getForm().getFormId() == 23) {
					patientSet.add(patient.getPatientId());
				}
			}
		}
		//List<Encounter> encounterList=encounterService.getEncountersByPatient(ptnt)
		return patientSet;
	}
	
	public Set<Integer> buildCohortByGender(String gender) {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		List<Patient> patientList = patientService.getAllPatients();
		for (Patient pts : patientList) {
			if (StringUtils.equalsIgnoreCase(pts.getGender(), gender)) {
				patientSet.add(pts.getPatientId());
			}
		}
		return patientSet;
	}
	
	public Set<Integer> buildCohortByDocumentedGender() {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		List<Patient> patientList = patientService.getAllPatients();
		for (Patient pts : patientList) {
			if (StringUtils.isNotEmpty(pts.getPerson().getGender())) {
				patientSet.add(pts.getPatientId());
			}
		}
		return patientSet;
	}
	
	public Set<Integer> buildCohortByDocumentedDOB() {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		List<Patient> patientList = patientService.getAllPatients();
		for (Patient pts : patientList) {
			if (pts.getPerson().getBirthdate() != null) {
				patientSet.add(pts.getPatientId());
			}
		}
		return patientSet;
	}
	
	public Set<Integer> buildCohortByActive() {
		Set<Integer> patientSet = new HashSet<Integer>();
		EncounterService encounterService = Context.getEncounterService();
		PatientService patientService = Context.getPatientService();
		Date startDate = null, endDate = new Date();
		DateTime startDateTime, endDateTime;
		endDateTime = new DateTime(endDate);
		startDateTime = endDateTime.minusMonths(4);
		List<Encounter> encounterList = new ArrayList<Encounter>();
		List<Patient> patientList = patientService.getAllPatients();
		//DateTime encounterDateTime = null;
		for (Patient pts : patientList) {
			encounterList = encounterService.getEncountersByPatient(pts);
			for (Encounter enc : encounterList) {
				//encounterDateTime = new DateTime(enc.getEncounterDatetime());
				if (isBetweenDate(startDateTime.toDate(), endDateTime.toDate(), enc.getEncounterDatetime())) {
					patientSet.add(enc.getPatient().getPatientId());
				}
			}
		}
		//encounterList.addAll(encounterService.getEncounters(startDateTime.toDate(), endDateTime.toDate()));
		
		return patientSet;
	}
	
	public Boolean isBetweenDate(Date startDate, Date endDate, Date checkDate) {
		Interval interval = new Interval(new DateTime(startDate), new DateTime(endDate));
		return interval.contains(new DateTime(checkDate));
	}
	
	public Set<Integer> buildCohortByAge(int startAge, int stopAge) {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		List<Patient> patientList = patientService.getAllPatients();
		int age = 0;
		for (Patient pts : patientList) {
			age = pts.getAge();
			
			if (age >= startAge && age < stopAge) {
				patientSet.add(pts.getPatientId());
			}
		}
		return patientSet;
	}
	
	public Set<Integer> buildCohortByAddress() {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		List<Patient> patientList = patientService.getAllPatients();
		Set<PersonAddress> addressSet = null;
		for (Patient pts : patientList) {
			addressSet = pts.getAddresses();
			if (!addressSet.isEmpty()) {
				patientSet.add(pts.getPatientId());
			}
		}
		return patientSet;
	}
	
	public List<Encounter> extractEncounters(Integer[] formIDArr, List<Encounter> encounterList) {
		List<Integer> targetEncounterTypeList = new ArrayList<Integer>();
		targetEncounterTypeList.addAll(Arrays.asList(formIDArr));
		List<Encounter> targetEncounters = new ArrayList<Encounter>();
		if (encounterList != null && !encounterList.isEmpty()) {
			for (Encounter enc : encounterList) {
				if (enc != null && enc.getForm() != null && targetEncounterTypeList != null
				        && !targetEncounterTypeList.isEmpty() && targetEncounterTypeList.contains(enc.getForm().getFormId())) {
					targetEncounters.add(enc);
				}
			}
		}
		return targetEncounters;
	}
	
	public Set<Integer> buildCohortByDateConcept(int conceptID, Integer[] formIDArr, Date startDate, Date endDate) {
		Set<Integer> patientSet = new HashSet<Integer>();
		PatientService patientService = Context.getPatientService();
		EncounterService encounterService = Context.getEncounterService();
		ObsService obsService = Context.getObsService();
		//obsService.getObservationsByPerson(person)
		List<Patient> patientList = patientService.getAllPatients();
		List<Encounter> encounterList = null, targetEncounterList = null;
		List<Obs> obsList = null;
		Obs obs = null;
		for (Patient pts : patientList) {
			//encounterList = encounterService.getEncountersByPatient(pts);
			obsList = obsService.getObservationsByPerson(pts);
			obs = extractObs(conceptID, obsList);
			if (obs != null) {
				if (isBetweenDate(startDate, endDate, obs.getValueDate())) {
					patientSet.add(pts.getPatientId());
					//break;
				}
			}
			/*if (encounterList != null && !encounterList.isEmpty()) {
				targetEncounterList = extractEncounters(formIDArr, encounterList);
				if (targetEncounterList != null && targetEncounterList.isEmpty()) {
					for (Encounter enc : targetEncounterList) {
						obsList = new ArrayList<Obs>(enc.getAllObs());
						obs = extractObs(conceptID, obsList);
						if (obs != null) {
							if (isBetweenDate(startDate, endDate, obs.getValueDate())) {
								patientSet.add(pts.getPatientId());
								//break;
							}
						}
					}
				}
				
			}*/
			
		}
		return patientSet;
	}
	
	public static Set<Integer> interset(Set<Integer> set1, Set<Integer> set2) {
		Set<Integer> ansSet = new HashSet<Integer>();
		ansSet = Sets.intersection(set1, set2);
		return ansSet;
	}
	
	public static Set<Integer> union(Set<Integer> set1, Set<Integer> set2) {
		Set<Integer> ansSet = new HashSet<Integer>();
		ansSet = Sets.union(set1, set2);
		return ansSet;
	}
	
	public static Obs extractObs(int conceptID, List<Obs> obsList) {

        if (obsList == null) {
            return null;
        }
        return obsList.stream().filter(ele -> ele.getConcept().getConceptId() == conceptID).findFirst().orElse(null);
    }
	
	public static Set<Integer> minus(Set<Integer> set1, Set<Integer> set2) {
		Set<Integer> ansSet = new HashSet<Integer>();
		ansSet = Sets.difference(set1, set2);
		return ansSet;
	}
	
	public void loadIndicatorNamesDictionary() {
		indicatorNamesMap.put(ACTIVE_COHORT, "Number of active patients");
		indicatorNamesMap.put(ACTIVE_DOCUMENTED_EDUCATIONAL_STATUS_COHORT,
		    "Number of All active  patients with a documented educational status");
		indicatorNamesMap.put(ACTIVE_DOCUMENTED_MARITAL_STATUS_COHORT,
		    "Proportion of all active patients with a documented marital status ");
		indicatorNamesMap.put(STARTED_ART_LAST_6MONTHS_DOCUMENTED_DOB,
		    "Proportion of patients newly started on ART in the last 6 months with documented age and/or Date of Birth");
	}
	
	public void loadCohortDictionary() {
		DateTime startDateTime = null, endDateTime = null;
		Set<Integer> activePatientCohort, documentedEducationalStatusCohort, documentedMaritalStatusCohort, documentedOccupationalStatusCohort;
		Set<Integer> answerSet = new HashSet<Integer>();
		activePatientCohort = buildCohortByActive();
		//Educational Status
		documentedEducationalStatusCohort = buildCohortByConceptID(EDUCATIONAL_STATUS_CONCEPT);
		answerSet = interset(activePatientCohort, documentedEducationalStatusCohort);
		cohortDictionary.put(ACTIVE_COHORT, activePatientCohort);
		cohortDictionary.put(DOCUMENTED_EDUCATIONAL_STATUS_COHORT, documentedEducationalStatusCohort);
		cohortDictionary.put(ACTIVE_DOCUMENTED_EDUCATIONAL_STATUS_COHORT, answerSet);
		//Marital Status Cohort
		documentedMaritalStatusCohort = buildCohortByConceptID(MARITAL_STATUS_CONCEPT);
		answerSet = interset(activePatientCohort, documentedMaritalStatusCohort);
		cohortDictionary.put(DOCUMENTED_MARITAL_STATUS_COHORT, documentedMaritalStatusCohort);
		cohortDictionary.put(ACTIVE_DOCUMENTED_MARITAL_STATUS_COHORT, answerSet);
		//Occupational Status
		documentedOccupationalStatusCohort = buildCohortByConceptID(OCCUPATIONAL_STATUS_CONCEPT);
		answerSet = interset(activePatientCohort, documentedOccupationalStatusCohort);
		cohortDictionary.put(DOCUMENTED_OCCUPATIONAL_STATUS_COHORT, documentedOccupationalStatusCohort);
		cohortDictionary.put(ACTIVE_DOCUMENTED_OCCUPATIONAL_STATUS_COHORT, answerSet);
		//Newly started on ART Last 6 Months with documented DOB
		endDateTime = new DateTime(new Date());
		startDateTime = endDateTime.minusMonths(6);
		Set<Integer> newlyStartedARTLast6MonthsCohort, patientsWithDocumentedAgeCohort;
		Integer[] formIDArr = { 23, 56 };
		newlyStartedARTLast6MonthsCohort = buildCohortByDateConcept(ART_START_DATE_CONCEPT, formIDArr,
		    startDateTime.toDate(), endDateTime.toDate());
		patientsWithDocumentedAgeCohort = buildCohortByDocumentedDOB();
		answerSet = interset(newlyStartedARTLast6MonthsCohort, patientsWithDocumentedAgeCohort);
		cohortDictionary.put(STARTED_ART_LAST_6MONTHS_COHORT, newlyStartedARTLast6MonthsCohort);
		cohortDictionary.put(STARTED_ART_LAST_6MONTHS_DOCUMENTED_DOB, answerSet);
		
		//Newly started on ART Last 6 Months with documented Gender
		endDateTime = new DateTime(new Date());
		startDateTime = endDateTime.minusMonths(6);
		Set<Integer> patientsWithDocumentedSexCohort;
		//newlyStartedARTLast6MonthsCohort = buildCohortByDateConcept(ART_START_DATE_CONCEPT, startDateTime.toDate(),
		//    endDateTime.toDate());
		patientsWithDocumentedSexCohort = buildCohortByDocumentedGender();
		answerSet = interset(newlyStartedARTLast6MonthsCohort, patientsWithDocumentedSexCohort);
		cohortDictionary.put(DOCUMENTED_SEX_COHORT, patientsWithDocumentedSexCohort);
		cohortDictionary.put(STARTED_ART_LAST_6MONTHS_DOCUMENTED_SEX, answerSet);
		
		//Newly started on ART Last 6 Months with documented DateConfirmedPositive
		endDateTime = new DateTime(new Date());
		startDateTime = endDateTime.minusMonths(6);
		Set<Integer> patientsWithDocumentedDateConfirmedPositiveCohort;
		//newlyStartedARTLast6MonthsCohort = buildCohortByDateConcept(ART_START_DATE_CONCEPT, startDateTime.toDate(),
		//    endDateTime.toDate());
		patientsWithDocumentedDateConfirmedPositiveCohort = buildCohortByConceptID(DATE_CONFIRMED_POSITIVE,
		    startDateTime.toDate(), endDateTime.toDate());
		answerSet = interset(newlyStartedARTLast6MonthsCohort, patientsWithDocumentedDateConfirmedPositiveCohort);
		cohortDictionary.put(DOCUMENTED_DIAGNOSIS_STATUS_COHORT, patientsWithDocumentedDateConfirmedPositiveCohort);
		cohortDictionary.put(STARTED_ART_LAST_6MONTHS_DOCUMENTED_DATECONFIRMED_POSITIVE, answerSet);
		
		//Proportion of patients newly started on ART in the last 6 months with documented HIV enrollment date
		//endDateTime = new DateTime(new Date());
		//startDateTime = endDateTime.minusMonths(6);
		Set<Integer> patientsWithDocumentedHIVEnrollmentDateCohort;
		//newlyStartedARTLast6MonthsCohort = buildCohortByDateConcept(ART_START_DATE_CONCEPT, startDateTime.toDate(),
		//    endDateTime.toDate());
		patientsWithDocumentedHIVEnrollmentDateCohort = buildCohortByHIVEnrollment();
		answerSet = interset(newlyStartedARTLast6MonthsCohort, patientsWithDocumentedHIVEnrollmentDateCohort);
		cohortDictionary.put(EVER_ENROLLED_IN_CARE_COHORT, patientsWithDocumentedHIVEnrollmentDateCohort);
		cohortDictionary.put(STARTED_ART_LAST_6MONTHS_DOCUMENTED_HIVENROLLMENT, answerSet);
		
	}
	
	public int countCohort(int cohortID) {
		int size = 0;
		if (cohortDictionary.containsKey(cohortID)) {
			size = cohortDictionary.get(cohortID).size();
		}
		return size;
		
	}
	
	public Set<Integer> getCohort(int cohortID) {
		Set<Integer> patientSet = new HashSet<Integer>();
		if (cohortDictionary.containsKey(cohortID)) {
			patientSet = cohortDictionary.get(cohortID);
		}
		return patientSet;
	}
	
	public String getIndicatorName(int cohortID) {
		String indicatorName = "";
		if (indicatorNamesMap.containsKey(cohortID)) {
			indicatorName = indicatorNamesMap.get(cohortID);
		}
		return indicatorName;
	}
	
	public double getPercentage(double numerator, double denominator) {
		double ans = numerator / denominator;
		ans = ans * 100;
		return ans;
	}
	
}
