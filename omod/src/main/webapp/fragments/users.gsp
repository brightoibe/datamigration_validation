<%
    def id = config.id
%>
<%=ui.resourceLinks()%>


<style type="text/css">
.dt-buttons{
    float: right;
}
#apps{
    margin-bottom: 60px;
}

.buttons-html5{
    text-decoration: none;
    margin-left: 5px;
    margin-bottom: 10px;
    text-align: center;
    border-radius: 3px;
    background: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #5b57a6), color-stop(100%, #5b57a6));
    background: -webkit-linear-gradient(top, #5b57a6, #5b57a6);
    background: -moz-linear-gradient(top, #5b57a6, #5b57a6);
    background: -o-linear-gradient(top, #5b57a6, #5b57a6);
    background: -ms-linear-gradient(top, #5b57a6, #5b57a6);
    background: linear-gradient(top, #5b57a6, #5b57a6);
    background-color: #5b57a6;
    border: #5b57a6 1px solid;
    padding: 8px 20px;
    display: inline-block;
    line-height: 1.2em;
    color: white;
    cursor: pointer;
    min-width: 0;
    max-width: 300px;
    text-decoration: none;
    padding: 5px 6px;
    min-width: 70px;
    font-size: 0.9em;
}

#myTable_paginate{
    display: flex-inline;
}
#myTable_paginate li {
    margin:2px;
    padding:3px;
    text-decoration: none;
        text-align: center;
        border-radius: 3px;
        background: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #5b57a6), color-stop(100%, #5b57a6));
        background: -webkit-linear-gradient(top, #5b57a6, #5b57a6);
        background: -moz-linear-gradient(top, #5b57a6, #5b57a6);
        background: -o-linear-gradient(top, #5b57a6, #5b57a6);
        background: -ms-linear-gradient(top, #5b57a6, #5b57a6);
        background: linear-gradient(top, #5b57a6, #5b57a6);
        background-color: #5b57a6;
        border: #5b57a6 1px solid;
        display: inline-block;
        color: white;
        cursor: pointer;
        width:auto;
}

#myTable_paginate li a{
 color:white;
}
</style>



<script>
    jq = jQuery;
    jq(document).ready(function () {
        jq('#myTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                'copy', 'excel', 'pdf'
            ]
        });
    });

</script>


<!--<div id="apps" align="center">

    <a id="demoapp-homepageLink-demoapp-homepageLink-extension" href="#"
       class="button app big">

        <i class="icon-user"></i>
        <br>

        Total Pateints
        <p><b>${ui.format(totalPatients)}</b></p>
    </a>

    <a id="coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension"
       href="#" class="button app big">

        <i class="icon-stethoscope"></i>
        <br>
        Lab Records
        <p><b>${ui.format(totallLaboratoryEncounter)}</b></p>
    </a>

    <a id="org-openmrs-module-coreapps-activeVisitsHomepageLink-org-openmrs-module-coreapps-activeVisitsHomepageLink-extension"
       href="#" class="button app big">

        <i class="icon-vitals"></i>
        <br>
        Pharmacy Records
        <p><b>${ui.format(totalPharmacyEncounter)}</b></p>
    </a>

    <a id="referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension"
       href="#"
       class="button app big">

        <i class="icon-calendar"></i>
        <br>
        Care card Records
        <p><b>${ui.format(totalCareCardEncounter)}</b></p>
    </a>

</div>-->
        <div class="container">
            <table id="myTable">
                <thead style="font-size: 13px;">
        <tr>
            <th>Indicator</th>
            <th>Numerator</th>
            <th>Denominator</th>
            <th>Performance</th>
            <th>Export</th>
            
        </tr>
        </thead>
        <tbody>
            <tr>
                <td>Proportion of all active patients with a documented educational status </td>
                <td>${ui.format(activepatientseducationalstatuscount)}</td>
                <td>${ui.format(activepatientcount)}</td>
                <td>${ui.format(percentageeducationalstatus)}%</td>
                <td><a href=''>Download</a></td>
            </tr>
            <tr>
                <td>Proportion of all active patients with a documented marital status </td>
                <td>${ui.format(activepatientsmaritalstatuscount)}</td>
                <td>${ui.format(activepatientcount)}</td>
                <td>${ui.format(percentagemaritalstatus)}%</td>
                <td><a href=''>Download</a></td>
            </tr>
            <tr>
                <td>Proportion of all active patients with a documented occupational status </td>
                <td>${ui.format(activepatientsoccupationalstatuscount)}</td>
                <td>${ui.format(activepatientcount)}</td>
                <td>${ui.format(percentageoccupationalstatus)}%</td>
                <td><a href=''>Download</a></td>
            </tr>
            <tr>
                <td>Proportion of patients newly started on ART in the last 6 months with documented age and/or Date of Birth</td>
                <td>${ui.format(startedartlast6monthscountdocumenteddob)}</td>
                <td>${ui.format(startedartlast6monthscount)}</td>
                <td>${ui.format(percentagestartedartlast6monthswithdocumenteddob)}%</td>
                <td><a href=''>Download</a></td>
            </tr>
            <tr>
                <td>Proportion of patients newly started on ART in the last 6 months with documented Sex</td>
                <td>${ui.format(startedartlast6monthscountdocumentedsex)}</td>
                <td>${ui.format(startedartlast6monthscount)}</td>
                <td>${ui.format(percentagestartedartlast6monthswithdocumentedsex)}%</td>
                <td><a href=''>Download</a></td>
            </tr>
            <tr>
                <td>Proportion of patients newly started on ART in the last 6 months with registered address/LGA of residence </td>
                <td></td>
                <td></td>
                <td></td>
                <td><a href=''>Download</a></td>
            </tr>
            <tr>
                <td>Proportion of patients newly started on ART in the last 6 months with documented date of HIV diagnosis</td>
                <td></td>
                <td>${ui.format(startedartlast6monthscount)}</td>
                <td></td>
                <td><a href=''>Download</a></td>
            </tr>
            
        </tbody>
                </table>
        </div>
<!--<div class="container">

    <table id="myTable">
        <thead style="font-size: 13px;">
        <tr>
            <th>PatientID</th>
            <th>Pateint Name</th>
            <th>Lab records</th>
            <th>Pharmacy Records</th>
            <th>ARTstartdate</th>
            <th>LastPickup</th>
            <th>firstDocumentedRegimen</th>
            <th>lastDocumentedRegimen</th>
        </tr>
        </thead>
        <tbody>
        <% if (patientLineList) { %>
        <% patientLineList.each { %>
        <tr>
            <td>${ui.format(it.PatientId)}</td>
            <td>${ui.format(it.PatientName)}</td>
            <td>${ui.format(it.countOfLabEncounter)}</td>
            <td>${ui.format(it.countOfPharmacyEncounter)}</td>
            <td>${ui.format(it.dateOfFirstEncounter)}</td>
            <td>${ui.format(it.dateOfLastEncounter)}</td>
            <td>${ui.format(it.firstDocumentedRegimen)}</td>
            <td>${ui.format(it.lastDocumentedRegimen)}</td>
        </tr>
        <% } %>
        <% } else { %>
        <tr>
            <td colspan="8" align="center">No Record To display</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>-->











