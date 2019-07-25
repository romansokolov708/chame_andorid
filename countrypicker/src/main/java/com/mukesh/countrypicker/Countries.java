package com.mukesh.countrypicker;

import java.util.ArrayList;
import java.util.List;

public class Countries {

    public static Country[] getAllCountries() {
        return COUNTRIES;
    }

    public static Country[] getCountries(String[] countryNames) {
        List<Country> filteredCountries = new ArrayList<>();
        Country[] allCountries = getAllCountries();
        for (String cn : countryNames) {
            for (Country c : allCountries) {
                if (cn.equals(c.getName())) {
                    filteredCountries.add(c);
                }
            }
        }
        return (Country[]) filteredCountries.toArray();
    }

    public static Country getCountryByName(String countryName) {
        Country[] allCountries = getAllCountries();
        int idx = -1;
        for (int i=0;i<allCountries.length;i++) {
            if (allCountries[i].getName().equals(countryName)) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            return allCountries[0];
        } else {
            return allCountries[idx];
        }
    }

    public static Country getCountryByCodeTxt(String codeTxt) {
        Country[] allCountries = getAllCountries();
        int idx = -1;
        for (int i=0;i<allCountries.length;i++) {
            if (allCountries[i].getCode().equals(codeTxt)) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            return allCountries[0];
        } else {
            return allCountries[idx];
        }
    }

    public static Country getCountryByPhoneCode(String phoneCode) {
        Country[] allCountries = getAllCountries();
        int idx = -1;
        for (int i=0;i<allCountries.length;i++) {
            if (allCountries[i].getDialCode().equals(phoneCode)) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            return allCountries[0];
        } else {
            return allCountries[idx];
        }
    }

    public static List<Country> getCountriesByCountryNames(String[] countryNames) {
        List<Country> countries = new ArrayList<>();

        for (String countryname : countryNames) {
            for (Country country : COUNTRIES) {
                if (countryname.equals(country.getName())) {
                    countries.add(country);
                }
            }
        }

        return countries;
    }

    // region Countries
    public static final Country[] COUNTRIES = {
            new Country("AD", "Andorra", "+376", "020", R.drawable.flag_ad, "EUR"),
            new Country("AE", "United Arab Emirates", "+971", "784", R.drawable.flag_ae, "AED"),
            new Country("AF", "Afghanistan", "+93", "004", R.drawable.flag_af, "AFN"),
            new Country("AG", "Antigua and Barbuda", "+1", "028", R.drawable.flag_ag, "XCD"),
            new Country("AI", "Anguilla", "+1", "660", R.drawable.flag_ai, "XCD"),
            new Country("AL", "Albania", "+355", "008", R.drawable.flag_al, "ALL"),
            new Country("AM", "Armenia", "+374", "051", R.drawable.flag_am, "AMD"),
            new Country("AO", "Angola", "+244", "024", R.drawable.flag_ao, "AOA"),
            new Country("AQ", "Antarctica", "+672", "010", R.drawable.flag_aq, "USD"),
            new Country("AR", "Argentina", "+54", "032", R.drawable.flag_ar, "ARS"),
            new Country("AS", "American Samoa", "+1", "016", R.drawable.flag_as, "USD"),
            new Country("AT", "Austria", "+43", "040", R.drawable.flag_at, "EUR"),
            new Country("AU", "Australia", "+61", "036", R.drawable.flag_au, "AUD"),
            new Country("AW", "Aruba", "+297", "533", R.drawable.flag_aw, "AWG"),
            new Country("AX", "Aland Islands", "+358", "248", R.drawable.flag_ax, "EUR"),
            new Country("AZ", "Azerbaijan", "+994", "031", R.drawable.flag_az, "AZN"),
            new Country("BA", "Bosnia and Herzegovina", "+387", "070", R.drawable.flag_ba, "BAM"),
            new Country("BB", "Barbados", "+1", "052", R.drawable.flag_bb, "BBD"),
            new Country("BD", "Bangladesh", "+880", "050", R.drawable.flag_bd, "BDT"),
            new Country("BE", "Belgium", "+32", "056", R.drawable.flag_be, "EUR"),
            new Country("BF", "Burkina Faso", "+226", "854", R.drawable.flag_bf, "XOF"),
            new Country("BG", "Bulgaria", "+359", "100", R.drawable.flag_bg, "BGN"),
            new Country("BH", "Bahrain", "+973", "048", R.drawable.flag_bh, "BHD"),
            new Country("BI", "Burundi", "+257", "108", R.drawable.flag_bi, "BIF"),
            new Country("BJ", "Benin", "+229", "204", R.drawable.flag_bj, "XOF"),
            new Country("BL", "Saint Barthelemy", "+590", "652", R.drawable.flag_bl, "EUR"),
            new Country("BM", "Bermuda", "+1", "060", R.drawable.flag_bm, "BMD"),
            new Country("BN", "Brunei Darussalam", "+673", "673", R.drawable.flag_bn, "BND"),
            new Country("BO", "Bolivia, Plurinational State of", "+591", "068", R.drawable.flag_bo, "BOB"),
            new Country("BR", "Brazil", "+55", "076", R.drawable.flag_br, "BRL"),
            new Country("BS", "Bahamas", "+1", "044", R.drawable.flag_bs, "BSD"),
            new Country("BT", "Bhutan", "+975", "064", R.drawable.flag_bt, "BTN"),
            new Country("BW", "Botswana", "+267", "072", R.drawable.flag_bw, "BWP"),
            new Country("BY", "Belarus", "+375", "112", R.drawable.flag_by, "BYR"),
            new Country("BZ", "Belize", "+501", "084", R.drawable.flag_bz, "BZD"),
            new Country("CA", "Canada", "+1", "124", R.drawable.flag_ca, "CAD"),
            new Country("CC", "Cocos (Keeling) Islands", "+61", "166", R.drawable.flag_cc, "AUD"),
            new Country("CD", "Congo, The Democratic Republic of the", "+243", "180", R.drawable.flag_cd, "CDF"),
            new Country("CF", "Central African Republic", "+236", "140", R.drawable.flag_cf, "XAF"),
            new Country("CG", "Congo", "+242", "178", R.drawable.flag_cg, "XAF"),
            new Country("CH", "Switzerland", "+41", "756", R.drawable.flag_ch, "CHF"),
            new Country("CI", "Ivory Coast", "+225", "384", R.drawable.flag_ci, "XOF"),
            new Country("CK", "Cook Islands", "+682", "184", R.drawable.flag_ck, "NZD"),
            new Country("CL", "Chile", "+56", "152", R.drawable.flag_cl, "CLP"),
            new Country("CM", "Cameroon", "+237", "120", R.drawable.flag_cm, "XAF"),
            new Country("CN", "China", "+86", "156", R.drawable.flag_cn, "CNY"),
            new Country("CO", "Colombia", "+57", "170", R.drawable.flag_co, "COP"),
            new Country("CR", "Costa Rica", "+506", "188", R.drawable.flag_cr, "CRC"),
            new Country("CU", "Cuba", "+53", "192", R.drawable.flag_cu, "CUP"),
            new Country("CV", "Cape Verde", "+238", "132", R.drawable.flag_cv, "CVE"),
            new Country("CX", "Christmas Island", "+61", "162", R.drawable.flag_cx, "AUD"),
            new Country("CY", "Cyprus", "+357", "196", R.drawable.flag_cy, "EUR"),
            new Country("CZ", "Czech Republic", "+420", "203", R.drawable.flag_cz, "CZK"),
            new Country("DE", "Germany", "+49", "276", R.drawable.flag_de, "EUR"),
            new Country("DJ", "Djibouti", "+253", "262", R.drawable.flag_dj, "DJF"),
            new Country("DK", "Denmark", "+45", "208", R.drawable.flag_dk, "DKK"),
            new Country("DM", "Dominica", "+1", "212", R.drawable.flag_dm, "XCD"),
            new Country("DO", "Dominican Republic", "+1", "214", R.drawable.flag_do, "DOP"),
            new Country("DZ", "Algeria", "+213", "012", R.drawable.flag_dz, "DZD"),
            new Country("EC", "Ecuador", "+593", "218", R.drawable.flag_ec, "USD"),
            new Country("EE", "Estonia", "+372", "233", R.drawable.flag_ee, "EUR"),
            new Country("EG", "Egypt", "+20", "818", R.drawable.flag_eg, "EGP"),
            new Country("EH", "Western Sahara", "+212", "732", R.drawable.flag_eh, "MAD"),
            new Country("ER", "Eritrea", "+291", "232", R.drawable.flag_er, "ERN"),
            new Country("ES", "Spain", "+34", "724", R.drawable.flag_es, "EUR"),
            new Country("ET", "Ethiopia", "+251", "231", R.drawable.flag_et, "ETB"),
            new Country("FI", "Finland", "+358", "246", R.drawable.flag_fi, "EUR"),
            new Country("FJ", "Fiji", "+679", "242", R.drawable.flag_fj, "FJD"),
            new Country("FK", "Falkland Islands (Malvinas)", "+500", "238", R.drawable.flag_fk, "FKP"),
            new Country("FM", "Micronesia, Federated States of", "+691", "583", R.drawable.flag_fm, "USD"),
            new Country("FO", "Faroe Islands", "+298", "234", R.drawable.flag_fo, "DKK"),
            new Country("FR", "France", "+33", "250", R.drawable.flag_fr, "EUR"),
            new Country("GA", "Gabon", "+241", "266", R.drawable.flag_ga, "XAF"),
            new Country("GB", "United Kingdom", "+44", "826", R.drawable.flag_gb, "GBP"),
            new Country("GD", "Grenada", "+1", "308", R.drawable.flag_gd, "XCD"),
            new Country("GE", "Georgia", "+995", "268", R.drawable.flag_ge, "GEL"),
            new Country("GF", "French Guiana", "+594", "254", R.drawable.flag_gf, "EUR"),
            new Country("GG", "Guernsey", "+44", "831", R.drawable.flag_gg, "GGP"),
            new Country("GH", "Ghana", "+233", "288", R.drawable.flag_gh, "GHS"),
            new Country("GI", "Gibraltar", "+350", "292", R.drawable.flag_gi, "GIP"),
            new Country("GL", "Greenland", "+299", "304", R.drawable.flag_gl, "DKK"),
            new Country("GM", "Gambia", "+220", "270", R.drawable.flag_gm, "GMD"),
            new Country("GN", "Guinea", "+224", "324", R.drawable.flag_gn, "GNF"),
            new Country("GP", "Guadeloupe", "+590", "312", R.drawable.flag_gp, "EUR"),
            new Country("GQ", "Equatorial Guinea", "+240", "226", R.drawable.flag_gq, "XAF"),
            new Country("GR", "Greece", "+30", "300", R.drawable.flag_gr, "EUR"),
            new Country("GS", "South Georgia and the South Sandwich Islands", "+500", "239", R.drawable.flag_gs,
                    "GBP"),
            new Country("GT", "Guatemala", "+502", "320", R.drawable.flag_gt, "GTQ"),
            new Country("GU", "Guam", "+1", "316", R.drawable.flag_gu, "USD"),
            new Country("GW", "Guinea-Bissau", "+245", "624", R.drawable.flag_gw, "XOF"),
            new Country("GY", "Guyana", "+595", "328", R.drawable.flag_gy, "GYD"),
            new Country("HK", "Hong Kong", "+852", "344", R.drawable.flag_hk, "HKD"),
            new Country("HM", "Heard Island and McDonald Islands", "+000", "334", R.drawable.flag_hm, "AUD"),
            new Country("HN", "Honduras", "+504", "340", R.drawable.flag_hn, "HNL"),
            new Country("HR", "Croatia", "+385", "191", R.drawable.flag_hr, "HRK"),
            new Country("HT", "Haiti", "+509", "332", R.drawable.flag_ht, "HTG"),
            new Country("HU", "Hungary", "+36", "348", R.drawable.flag_hu, "HUF"),
            new Country("ID", "Indonesia", "+62", "360", R.drawable.flag_id, "IDR"),
            new Country("IE", "Ireland", "+353", "372", R.drawable.flag_ie, "EUR"),
            new Country("IL", "Israel", "+972", "376", R.drawable.flag_il, "ILS"),
            new Country("IM", "Isle of Man", "+44", "833", R.drawable.flag_im, "GBP"),
            new Country("IN", "India", "+91", "356", R.drawable.flag_in, "INR"),
            new Country("IO", "British Indian Ocean Territory", "+246", "086", R.drawable.flag_io, "USD"),
            new Country("IQ", "Iraq", "+964", "368", R.drawable.flag_iq, "IQD"),
            new Country("IR", "Iran, Islamic Republic of", "+98", "364", R.drawable.flag_ir, "IRR"),
            new Country("IS", "Iceland", "+354", "352", R.drawable.flag_is, "ISK"),
            new Country("IT", "Italy", "+39", "380", R.drawable.flag_it, "EUR"),
            new Country("JE", "Jersey", "+44", "832", R.drawable.flag_je, "JEP"),
            new Country("JM", "Jamaica", "+1", "388", R.drawable.flag_jm, "JMD"),
            new Country("JO", "Jordan", "+962", "400", R.drawable.flag_jo, "JOD"),
            new Country("JP", "Japan", "+81", "392", R.drawable.flag_jp, "JPY"),
            new Country("KE", "Kenya", "+254", "404", R.drawable.flag_ke, "KES"),
            new Country("KG", "Kyrgyzstan", "+996", "417", R.drawable.flag_kg, "KGS"),
            new Country("KH", "Cambodia", "+855", "116", R.drawable.flag_kh, "KHR"),
            new Country("KI", "Kiribati", "+686", "296", R.drawable.flag_ki, "AUD"),
            new Country("KM", "Comoros", "+269", "174", R.drawable.flag_km, "KMF"),
            new Country("KN", "Saint Kitts and Nevis", "+1", "659", R.drawable.flag_kn, "XCD"),
            new Country("KP", "North Korea", "+850", "408", R.drawable.flag_kp, "KPW"),
            new Country("KR", "South Korea", "+82", "410", R.drawable.flag_kr, "KRW"),
            new Country("KW", "Kuwait", "+965", "414", R.drawable.flag_kw, "KWD"),
            new Country("KY", "Cayman Islands", "+345", "136", R.drawable.flag_ky, "KYD"),
            new Country("KZ", "Kazakhstan", "+7", "398", R.drawable.flag_kz, "KZT"),
            new Country("LA", "Lao People's Democratic Republic", "+856", "418", R.drawable.flag_la, "LAK"),
            new Country("LB", "Lebanon", "+961", "422", R.drawable.flag_lb, "LBP"),
            new Country("LC", "Saint Lucia", "+1", "662", R.drawable.flag_lc, "XCD"),
            new Country("LI", "Liechtenstein", "+423", "438", R.drawable.flag_li, "CHF"),
            new Country("LK", "Sri Lanka", "+94", "144", R.drawable.flag_lk, "LKR"),
            new Country("LR", "Liberia", "+231", "430", R.drawable.flag_lr, "LRD"),
            new Country("LS", "Lesotho", "+266", "426", R.drawable.flag_ls, "LSL"),
            new Country("LT", "Lithuania", "+370", "440", R.drawable.flag_lt, "LTL"),
            new Country("LU", "Luxembourg", "+352", "442", R.drawable.flag_lu, "EUR"),
            new Country("LV", "Latvia", "+371", "428", R.drawable.flag_lv, "LVL"),
            new Country("LY", "Libyan Arab Jamahiriya", "+218", "434", R.drawable.flag_ly, "LYD"),
            new Country("MA", "Morocco", "+212", "504", R.drawable.flag_ma, "MAD"),
            new Country("MC", "Monaco", "+377", "492", R.drawable.flag_mc, "EUR"),
            new Country("MD", "Moldova, Republic of", "+373", "498", R.drawable.flag_md, "MDL"),
            new Country("ME", "Montenegro", "+382", "499", R.drawable.flag_me, "EUR"),
            new Country("MF", "Saint Martin", "+590", "663", R.drawable.flag_mf, "EUR"),
            new Country("MG", "Madagascar", "+261", "450", R.drawable.flag_mg, "MGA"),
            new Country("MH", "Marshall Islands", "+692", "584", R.drawable.flag_mh, "USD"),
            new Country("MK", "Macedonia, The Former Yugoslav Republic of", "+389", "807", R.drawable.flag_mk,
                    "MKD"),
            new Country("ML", "Mali", "+223", "466", R.drawable.flag_ml, "XOF"),
            new Country("MM", "Myanmar", "+95", "104", R.drawable.flag_mm, "MMK"),
            new Country("MN", "Mongolia", "+976", "496", R.drawable.flag_mn, "MNT"),
            new Country("MO", "Macao", "+853", "446", R.drawable.flag_mo, "MOP"),
            new Country("MP", "Northern Mariana Islands", "+1", "580", R.drawable.flag_mp, "USD"),
            new Country("MQ", "Martinique", "+596", "474", R.drawable.flag_mq, "EUR"),
            new Country("MR", "Mauritania", "+222", "478", R.drawable.flag_mr, "MRO"),
            new Country("MS", "Montserrat", "+1", "500", R.drawable.flag_ms, "XCD"),
            new Country("MT", "Malta", "+356", "470", R.drawable.flag_mt, "EUR"),
            new Country("MU", "Mauritius", "+230", "480", R.drawable.flag_mu, "MUR"),
            new Country("MV", "Maldives", "+960", "462", R.drawable.flag_mv, "MVR"),
            new Country("MW", "Malawi", "+265", "454", R.drawable.flag_mw, "MWK"),
            new Country("MX", "Mexico", "+52", "484", R.drawable.flag_mx, "MXN"),
            new Country("MY", "Malaysia", "+60", "458", R.drawable.flag_my, "MYR"),
            new Country("MZ", "Mozambique", "+258", "508", R.drawable.flag_mz, "MZN"),
            new Country("NA", "Namibia", "+264", "516", R.drawable.flag_na, "NAD"),
            new Country("NC", "New Caledonia", "+687", "540", R.drawable.flag_nc, "XPF"),
            new Country("NE", "Niger", "+227", "562", R.drawable.flag_ne, "XOF"),
            new Country("NF", "Norfolk Island", "+672", "574", R.drawable.flag_nf, "AUD"),
            new Country("NG", "Nigeria", "+234", "566", R.drawable.flag_ng, "NGN"),
            new Country("NI", "Nicaragua", "+505", "558", R.drawable.flag_ni, "NIO"),
            new Country("NL", "Netherlands", "+31", "528", R.drawable.flag_nl, "EUR"),
            new Country("NO", "Norway", "+47", "578", R.drawable.flag_no, "NOK"),
            new Country("NP", "Nepal", "+977", "524", R.drawable.flag_np, "NPR"),
            new Country("NR", "Nauru", "+674", "520", R.drawable.flag_nr, "AUD"),
            new Country("NU", "Niue", "+683", "570", R.drawable.flag_nu, "NZD"),
            new Country("NZ", "New Zealand", "+64", "554", R.drawable.flag_nz, "NZD"),
            new Country("OM", "Oman", "+968", "512", R.drawable.flag_om, "OMR"),
            new Country("PA", "Panama", "+507", "591", R.drawable.flag_pa, "PAB"),
            new Country("PE", "Peru", "+51", "604", R.drawable.flag_pe, "PEN"),
            new Country("PF", "French Polynesia", "+689", "258", R.drawable.flag_pf, "XPF"),
            new Country("PG", "Papua New Guinea", "+675", "598", R.drawable.flag_pg, "PGK"),
            new Country("PH", "Philippines", "+63", "608", R.drawable.flag_ph, "PHP"),
            new Country("PK", "Pakistan", "+92", "586", R.drawable.flag_pk, "PKR"),
            new Country("PL", "Poland", "+48", "616", R.drawable.flag_pl, "PLN"),
            new Country("PM", "Saint Pierre and Miquelon", "+508", "666", R.drawable.flag_pm, "EUR"),
            new Country("PN", "Pitcairn", "+872", "612", R.drawable.flag_pn, "NZD"),
            new Country("PR", "Puerto Rico", "+1", "630", R.drawable.flag_pr, "USD"),
            new Country("PS", "Palestinian Territory, Occupied", "+970", "275", R.drawable.flag_ps, "ILS"),
            new Country("PT", "Portugal", "+351", "620", R.drawable.flag_pt, "EUR"),
            new Country("PW", "Palau", "+680", "585", R.drawable.flag_pw, "USD"),
            new Country("PY", "Paraguay", "+595", "600", R.drawable.flag_py, "PYG"),
            new Country("QA", "Qatar", "+974", "634", R.drawable.flag_qa, "QAR"),
            new Country("RE", "Reunion", "+262", "638", R.drawable.flag_re, "EUR"),
            new Country("RO", "Romania", "+40", "642", R.drawable.flag_ro, "RON"),
            new Country("RS", "Serbia", "+381", "688", R.drawable.flag_rs, "RSD"),
            new Country("RU", "Russia", "+7", "643", R.drawable.flag_ru, "RUB"),
            new Country("RW", "Rwanda", "+250", "646", R.drawable.flag_rw, "RWF"),
            new Country("SA", "Saudi Arabia", "+966", "682", R.drawable.flag_sa, "SAR"),
            new Country("SB", "Solomon Islands", "+677", "090", R.drawable.flag_sb, "SBD"),
            new Country("SC", "Seychelles", "+248", "690", R.drawable.flag_sc, "SCR"),
            new Country("SD", "Sudan", "+249", "729", R.drawable.flag_sd, "SDG"),
            new Country("SE", "Sweden", "+46", "752", R.drawable.flag_se, "SEK"),
            new Country("SG", "Singapore", "+65", "702", R.drawable.flag_sg, "SGD"),
            new Country("SH", "Saint Helena, Ascension and Tristan Da Cunha", "+290", "654", R.drawable.flag_sh,
                    "SHP"),
            new Country("SI", "Slovenia", "+386", "705", R.drawable.flag_si, "EUR"),
            new Country("SJ", "Svalbard and Jan Mayen", "+47", "744", R.drawable.flag_sj, "NOK"),
            new Country("SK", "Slovakia", "+421", "703", R.drawable.flag_sk, "EUR"),
            new Country("SL", "Sierra Leone", "+232", "694", R.drawable.flag_sl, "SLL"),
            new Country("SM", "San Marino", "+378", "674", R.drawable.flag_sm, "EUR"),
            new Country("SN", "Senegal", "+221", "686", R.drawable.flag_sn, "XOF"),
            new Country("SO", "Somalia", "+252", "706", R.drawable.flag_so, "SOS"),
            new Country("SR", "Suriname", "+597", "740", R.drawable.flag_sr, "SRD"),
            new Country("SS", "South Sudan", "+211", "728", R.drawable.flag_ss, "SSP"),
            new Country("ST", "Sao Tome and Principe", "+239", "678", R.drawable.flag_st, "STD"),
            new Country("SV", "El Salvador", "+503", "222", R.drawable.flag_sv, "SVC"),
            new Country("SY", "Syrian Arab Republic", "+963", "760", R.drawable.flag_sy, "SYP"),
            new Country("SZ", "Swaziland", "+268", "748", R.drawable.flag_sz, "SZL"),
            new Country("TC", "Turks and Caicos Islands", "+1", "796", R.drawable.flag_tc, "USD"),
            new Country("TD", "Chad", "+235", "148", R.drawable.flag_td, "XAF"),
            new Country("TF", "French Southern Territories", "+262", "260", R.drawable.flag_tf, "EUR"),
            new Country("TG", "Togo", "+228", "768", R.drawable.flag_tg, "XOF"),
            new Country("TH", "Thailand", "+66", "764", R.drawable.flag_th, "THB"),
            new Country("TJ", "Tajikistan", "+992", "762", R.drawable.flag_tj, "TJS"),
            new Country("TK", "Tokelau", "+690", "772", R.drawable.flag_tk, "NZD"),
            new Country("TL", "East Timor", "+670", "626", R.drawable.flag_tl, "USD"),
            new Country("TM", "Turkmenistan", "+993", "795", R.drawable.flag_tm, "TMT"),
            new Country("TN", "Tunisia", "+216", "788", R.drawable.flag_tn, "TND"),
            new Country("TO", "Tonga", "+676", "776", R.drawable.flag_to, "TOP"),
            new Country("TR", "Turkey", "+90", "792", R.drawable.flag_tr, "TRY"),
            new Country("TT", "Trinidad and Tobago", "+1", "780", R.drawable.flag_tt, "TTD"),
            new Country("TV", "Tuvalu", "+688", "798", R.drawable.flag_tv, "AUD"),
            new Country("TW", "Taiwan", "+886", "158", R.drawable.flag_tw, "TWD"),
            new Country("TZ", "Tanzania, United Republic of", "+255", "834", R.drawable.flag_tz, "TZS"),
            new Country("UA", "Ukraine", "+380", "804", R.drawable.flag_ua, "UAH"),
            new Country("UG", "Uganda", "+256", "800", R.drawable.flag_ug, "UGX"),
            new Country("US", "United States of America", "+1", "840", R.drawable.flag_us, "USD"),
            new Country("UY", "Uruguay", "+598", "858", R.drawable.flag_uy, "UYU"),
            new Country("UZ", "Uzbekistan", "+998", "860", R.drawable.flag_uz, "UZS"),
            new Country("VA", "Holy See (Vatican City State)", "+379", "336", R.drawable.flag_va, "EUR"),
            new Country("VC", "Saint Vincent and the Grenadines", "+1", "670", R.drawable.flag_vc, "XCD"),
            new Country("VE", "Venezuela, Bolivarian Republic of", "+58", "862", R.drawable.flag_ve, "VEF"),
            new Country("VG", "Virgin Islands, British", "+1", "092", R.drawable.flag_vg, "USD"),
            new Country("VI", "Virgin Islands, U.S.", "+1", "850", R.drawable.flag_vi, "USD"),
            new Country("VN", "Vietnam", "+84", "704", R.drawable.flag_vn, "VND"),
            new Country("VU", "Vanuatu", "+678", "548", R.drawable.flag_vu, "VUV"),
            new Country("WF", "Wallis and Futuna", "+681", "876", R.drawable.flag_wf, "XPF"),
            new Country("WS", "Samoa", "+685", "882", R.drawable.flag_ws, "WST"),
            new Country("YE", "Yemen", "+967", "887", R.drawable.flag_ye, "YER"),
            new Country("YT", "Mayotte", "+262", "175", R.drawable.flag_yt, "EUR"),
            new Country("ZA", "South Africa", "+27", "710", R.drawable.flag_za, "ZAR"),
            new Country("ZM", "Zambia", "+260", "894", R.drawable.flag_zm, "ZMW"),
            new Country("ZW", "Zimbabwe", "+263", "716", R.drawable.flag_zw, "USD"),
    };
    // endregion

    public static final String[] APP_COUNTRIES = {
            "Kenya",
            "United States of America",
            "United Kingdom",
            "Germany",
            "United Arab Emirates"
    };
}
