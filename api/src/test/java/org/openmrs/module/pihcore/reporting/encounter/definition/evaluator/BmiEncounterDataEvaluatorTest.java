package org.openmrs.module.pihcore.reporting.encounter.definition.evaluator;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.contrib.testdata.builder.EncounterBuilder;
import org.openmrs.contrib.testdata.builder.PatientBuilder;
import org.openmrs.module.pihcore.metadata.Metadata;
import org.openmrs.module.pihcore.metadata.core.EncounterTypes;
import org.openmrs.module.pihcore.metadata.haiti.HaitiPatientIdentifierTypes;
import org.openmrs.module.pihcore.metadata.haiti.mirebalais.MirebalaisLocations;
import org.openmrs.module.pihcore.reporting.BaseReportTest;
import org.openmrs.module.pihcore.reporting.encounter.definition.BmiEncounterDataDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.service.EncounterDataService;
import org.openmrs.module.reporting.evaluation.context.EncounterEvaluationContext;
import org.openmrs.module.reporting.query.encounter.EncounterIdSet;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class BmiEncounterDataEvaluatorTest extends BaseReportTest {

    @Autowired
    EncounterDataService encounterDataService;

    @Before
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void shouldCalculateBmi() throws Exception {

        EncounterBuilder eb = data.encounter();
        eb.patient(createPatient());
        eb.encounterDatetime(DateUtil.getDateTime(2015, 4, 15));
        eb.location(Metadata.lookup(MirebalaisLocations.CDI_KLINIK_EKSTEN_JENERAL));
        eb.encounterType(Metadata.lookup(EncounterTypes.VITALS));
        eb.obs(Metadata.getConcept("PIH:WEIGHT (KG)"), 45.4);
        eb.obs(Metadata.getConcept("PIH:HEIGHT (CM)"), 152.4);
        Encounter enc = eb.save();

        EncounterEvaluationContext context = new EncounterEvaluationContext();
        context.setBaseEncounters(new EncounterIdSet(enc.getEncounterId()));

        EvaluatedEncounterData res = encounterDataService.evaluate(new BmiEncounterDataDefinition(), context);

        assertThat((Double) res.getData().get(enc.getEncounterId()), is(19.5));
    }

    @Test
    public void shouldReturnNullIfNoWeight() throws Exception {

        EncounterBuilder eb = data.encounter();
        eb.patient(createPatient());
        eb.encounterDatetime(DateUtil.getDateTime(2015, 4, 15));
        eb.location(Metadata.lookup(MirebalaisLocations.CDI_KLINIK_EKSTEN_JENERAL));
        eb.encounterType(Metadata.lookup(EncounterTypes.VITALS));
        eb.obs(Metadata.getConcept("PIH:WEIGHT (KG)"), 45.4);
        Encounter enc = eb.save();

        EncounterEvaluationContext context = new EncounterEvaluationContext();
        context.setBaseEncounters(new EncounterIdSet(enc.getEncounterId()));

        EvaluatedEncounterData res = encounterDataService.evaluate(new BmiEncounterDataDefinition(), context);

        assertNull(res.getData().get(enc.getEncounterId()));
    }

    @Test
    public void shouldReturnNullIfNoHeight() throws Exception {

        EncounterBuilder eb = data.encounter();
        eb.patient(createPatient());
        eb.encounterDatetime(DateUtil.getDateTime(2015, 4, 15));
        eb.location(Metadata.lookup(MirebalaisLocations.CDI_KLINIK_EKSTEN_JENERAL));
        eb.encounterType(Metadata.lookup(EncounterTypes.VITALS));
        eb.obs(Metadata.getConcept("PIH:HEIGHT (CM)"), 152.4);
        Encounter enc = eb.save();

        EncounterEvaluationContext context = new EncounterEvaluationContext();
        context.setBaseEncounters(new EncounterIdSet(enc.getEncounterId()));

        EvaluatedEncounterData res = encounterDataService.evaluate(new BmiEncounterDataDefinition(), context);

        assertNull(res.getData().get(enc.getEncounterId()));
    }

}
