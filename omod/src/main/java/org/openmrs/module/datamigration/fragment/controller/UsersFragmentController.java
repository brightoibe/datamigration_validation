/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.datamigration.fragment.controller;

import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.datamigration.api.dao.DbConnection;
import org.openmrs.module.datamigration.util.FactoryUtils;
import org.openmrs.module.datamigration.util.Model.PatientLineList;
import org.openmrs.module.datamigration.util.Model.SummaryDashboard;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.module.datamigration.util.CohortMaster;

/**
 *  * Controller for a fragment that shows all users  
 */
public class UsersFragmentController {
	
	public void controller(FragmentModel model, @SpringBean("userService") UserService service) {

		DbConnection connection = new DbConnection();
		FactoryUtils factoryUtils = new FactoryUtils();
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<SummaryDashboard> summaryDashboardList = factoryUtils.getEncounters();
                CohortMaster cohortMaster=new CohortMaster();
		
                List<PatientLineList> patientLineList = factoryUtils.getPatientsLineList();

		model.addAttribute("patientLineList", patientLineList);
                
		map.put("totalPatients",Context.getPatientService().getAllPatients().size());
                double numerator=0.0,denominator=0.0;
                //Educational Status
                map.put("activepatientseducationalstatuscount",cohortMaster.getCohort(CohortMaster.ACTIVE_DOCUMENTED_EDUCATIONAL_STATUS_COHORT).size());
		map.put("activepatientcount",cohortMaster.getCohort(CohortMaster.ACTIVE_COHORT).size());
                numerator=cohortMaster.countCohort(CohortMaster.ACTIVE_DOCUMENTED_EDUCATIONAL_STATUS_COHORT);
                denominator=cohortMaster.countCohort(CohortMaster.ACTIVE_COHORT);
                map.put("percentageeducationalstatus",(int)cohortMaster.getPercentage(numerator,denominator));
                
                //Marital Status
                map.put("activepatientsmaritalstatuscount",cohortMaster.getCohort(CohortMaster.ACTIVE_DOCUMENTED_MARITAL_STATUS_COHORT).size());
		//map.put("activepatientcount",cohortMaster.getCohort(CohortMaster.ACTIVE_COHORT).size());
                numerator=cohortMaster.countCohort(CohortMaster.ACTIVE_DOCUMENTED_MARITAL_STATUS_COHORT);
                denominator=cohortMaster.countCohort(CohortMaster.ACTIVE_COHORT);
                map.put("percentagemaritalstatus",(int)cohortMaster.getPercentage(numerator,denominator));
                
                //Marital Status
                map.put("activepatientsoccupationalstatuscount",cohortMaster.getCohort(CohortMaster.ACTIVE_DOCUMENTED_OCCUPATIONAL_STATUS_COHORT).size());
		//map.put("activepatientcount",cohortMaster.getCohort(CohortMaster.ACTIVE_COHORT).size());
                numerator=cohortMaster.countCohort(CohortMaster.ACTIVE_DOCUMENTED_OCCUPATIONAL_STATUS_COHORT);
                denominator=cohortMaster.countCohort(CohortMaster.ACTIVE_COHORT);
                map.put("percentageoccupationalstatus",(int)cohortMaster.getPercentage(numerator,denominator));
                
                //Started ART Last 6 Months documented DOB
                numerator=cohortMaster.countCohort(CohortMaster.STARTED_ART_LAST_6MONTHS_DOCUMENTED_DOB);
                denominator=cohortMaster.countCohort(CohortMaster.STARTED_ART_LAST_6MONTHS_COHORT);
                map.put("startedartlast6monthscount",(int)denominator);
                map.put("startedartlast6monthscountdocumenteddob",(int)numerator);
                map.put("percentagestartedartlast6monthswithdocumenteddob",(int)cohortMaster.getPercentage(numerator,denominator));
                
                //Started ART Last 6 Months documented Gender
                numerator=cohortMaster.countCohort(CohortMaster.STARTED_ART_LAST_6MONTHS_DOCUMENTED_SEX);
                denominator=cohortMaster.countCohort(CohortMaster.STARTED_ART_LAST_6MONTHS_COHORT);
                map.put("startedartlast6monthscount",(int)denominator);
                map.put("startedartlast6monthscountdocumentedsex",(int)numerator);
                map.put("percentagestartedartlast6monthswithdocumentedsex",(int)cohortMaster.getPercentage(numerator,denominator));
                
                
                
                SummaryDashboard summaryDashboard = summaryDashboardList.stream().filter(x->x.getEncounterTypeID().equals(11)).findFirst().orElse(null);
		if(summaryDashboard != null)
			map.put("totallLaboratoryEncounter", summaryDashboard.getCountOfEncounter());
		else
			map.put("totallLaboratoryEncounter", 0);
		summaryDashboard = summaryDashboardList.stream().filter(x->x.getEncounterTypeID().equals(13)).findFirst().orElse(null);
		if(summaryDashboard != null)
			map.put("totalPharmacyEncounter", summaryDashboard.getCountOfEncounter());
		else
			map.put("totalPharmacyEncounter", 0);
		summaryDashboard = summaryDashboardList.stream().filter(x->x.getEncounterTypeID().equals(12)).findFirst().orElse(null);
		if(summaryDashboard != null)
			map.put("totalCareCardEncounter", summaryDashboard.getCountOfEncounter());
		else
			map.put("totalCareCardEncounter", 0);
		model.mergeAttributes(map);
		
	}
}
