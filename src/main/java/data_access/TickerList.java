package data_access;

import java.util.List;

public class TickerList {
    public static final List<String> TOP_100_TICKERS = List.of(
            // US Large Caps
            "JPM", "AAPL", "MSFT", "AMZN", "GOOGL", "GOOG",
            "XOM", "CVX", "WMT", "JNJ", "UNH", "PG", "NVDA", "HD",
            "BAC", "DIS", "PFE", "KO", "PEP", "TSLA", "VZ", "WFC",
            "T", "INTC", "CSCO", "V", "MA", "CMCSA", "ABT", "CRM",
            "MCD", "UPS", "PM", "COST", "MDT", "IBM", "TMO", "ORCL",
            "QCOM", "ACN", "COP", "RTX", "BA", "AMGN", "CAT", "LOW",

            // European / UK Large Caps
            "NESN.SW",   // Nestle
            "SHEL",      // Shell
            "UL",        // Unilever
            "HSBC",      // HSBC Holdings
            "SAP",       // SAP
            "BP",        // BP plc
            "BHP",       // BHP Group
            "AZN",       // AstraZeneca
            "DTEGY",     // Deutsche Telekom ADR
            "ALIZY",     // Allianz
            "SNY",       // Sanofi
            "DEO",       // Diageo
            "GLNCY",     // Glencore
            "NVO",       // Novo Nordisk
            "RIO",       // Rio Tinto

            // Japanese Large Caps
            "TM",        // Toyota
            "NSANY",     // Nissan
            "HMC",       // Honda
            "SNE",       // Sony
            "MITSY",     // Mitsubishi Corp ADR
            "SMFG",      // Sumitomo Mitsui

            // Korean Large Caps
            "SSNLF",     // Samsung Electronics
            "HYMTF",     // Hyundai Motor

            // Chinese Large Caps (H-Shares or US ADRs)
            "BABA",      // Alibaba
            "TCEHY",     // Tencent
            "PTR",       // PetroChina
            "BIDU",      // Baidu
            "JD",        // JD.com
            "PDD",       // Pinduoduo
            "HNP",        // Huaneng Power
            "CHA",        // China Telecom
            "CHL",        // China Mobile
            "CBRGF",      // China Construction Bank (OTC ADR)
            "IDCBY",      // ICBC (OTC ADR)

            // Canadian Large Caps
            "RY",        // Royal Bank of Canada
            "TD",        // Toronto-Dominion Bank
            "ENB",       // Enbridge
            "BMO",       // Bank of Montreal
            "BNS",       // Bank of Nova Scotia

            // Australian Large Caps
            "CBA.AX",    // Commonwealth Bank
            "WBC.AX",    // Westpac
            "NAB.AX",    // National Australia Bank
            "MQG.AX",    // Macquarie Group

            // Indian Large Caps (NSE symbols)
            "RELIANCE.NS",  // Reliance Industries
            "TCS.NS",       // Tata Consultancy
            "HDFCBANK.NS",  // HDFC Bank
            "INFY",         // Infosys (NYSE ADR)
            "WIT",          // Wipro (NYSE ADR)

            // Middle Eastern / Others
            "2222.SR",    // Saudi Aramco (Tadawul)
            "QNBK.QA",    // Qatar National Bank
            "SABIC.AB",   // SABIC
            "NCSM",       // New China Life (approx ADR)
            "MT",         // ArcelorMittal (Luxembourg, NYSE)

            // Latin American Large Caps
            "VALE",       // Vale SA
            "PBR",        // Petrobras
            "ITUB",       // Itau Unibanco
            "BBD",        // Banco Bradesco
            "AMX",        // América Móvil

            // Switzerland (some ADRs)
            "ABB",        // ABB Ltd
            "CS",         // Credit Suisse (legacy)
            "UBS"         // UBS Group
    );
}