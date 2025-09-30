package dev.luanfernandes.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathConstants {

    private static final String API_V1 = "/api/v1";

    public static final String VEHICLES = API_V1 + "/vehicles";
    public static final String VEHICLES_BY_ID_V1 = VEHICLES + "/{id}";
    public static final String VEHICLES_SEARCH_V1 = VEHICLES + "/search";
    public static final String VEHICLES_STATISTICS_V1 = VEHICLES + "/statistics";
    public static final String VEHICLES_STATISTICS_BY_DECADE_V1 = VEHICLES_STATISTICS_V1 + "/by-decade";
    public static final String VEHICLES_STATISTICS_BY_BRAND_V1 = VEHICLES_STATISTICS_V1 + "/by-brand";
    public static final String VEHICLES_STATISTICS_LAST_WEEK_V1 = VEHICLES_STATISTICS_V1 + "/last-week";
    public static final String VEHICLES_BRANDS_V1 = VEHICLES + "/brands";

    public static final String EXERCISES = API_V1 + "/exercises";
    public static final String EXERCISES_VOTING = EXERCISES + "/voting";
    public static final String EXERCISES_BUBBLE_SORT = EXERCISES + "/bubble-sort";
    public static final String EXERCISES_FATORIAL = EXERCISES + "/fatorial";
    public static final String EXERCISES_MULTIPLOS = EXERCISES + "/multiplos";
}
