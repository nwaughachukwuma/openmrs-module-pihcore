package org.openmrs.module.pihcore.setup;

import org.openmrs.api.LocationService;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.pihcore.config.Config;
import org.openmrs.module.pihcore.config.ConfigDescriptor;
import org.openmrs.module.pihcore.identifier.haiti.ConfigureHaitiIdGenerators;
import org.openmrs.module.pihcore.identifier.liberia.ConfigureLiberiaIdGenerators;
import org.openmrs.module.pihcore.identifier.sierraLeone.ConfigureSierraLeoneIdGenerators;

public class PatientIdentifierSetup {

    public static final String ARCHIVES = "archives";

    public static void setupIdentifierGeneratorsIfNecessary(IdentifierSourceService identifierSourceService,
                                                      LocationService locationService,
                                                      Config config) {

        if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
            ConfigureHaitiIdGenerators configureHaitiIdGenerators = new ConfigureHaitiIdGenerators(identifierSourceService);
            ConfigureHaitiIdGenerators.createPatientIdGenerator(configureHaitiIdGenerators);
            ConfigureHaitiIdGenerators.createDossierNumberGenerator(locationService, configureHaitiIdGenerators, config);
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.LIBERIA)) {
            ConfigureLiberiaIdGenerators.configureGenerators(identifierSourceService);
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.SIERRA_LEONE)) {
            ConfigureSierraLeoneIdGenerators.configureGenerators(identifierSourceService);
        }
    }
}
