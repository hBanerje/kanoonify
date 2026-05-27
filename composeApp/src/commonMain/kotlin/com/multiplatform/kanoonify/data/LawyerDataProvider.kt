package com.multiplatform.kanoonify.data

import com.multiplatform.kanoonify.domain.model.Lawyer

/**
 * Static in-memory list of dummy lawyers for the "Consult a lawyer" screen.
 * Replace with a network / database source when real registration backend is ready.
 */
object LawyerDataProvider {

    val lawyers: List<Lawyer> = listOf(
        Lawyer(
            id = "lw_001",
            name = "Adv. Priya Sharma",
            specialization = "Criminal Law",
            experienceYears = 12,
            rating = 4.8f,
            location = "New Delhi",
            languages = listOf("English", "Hindi"),
            feePerSession = 1500,
            isOnline = true,
            bio = "Specialises in criminal defence and bail matters. Practises at Delhi High Court."
        ),
        Lawyer(
            id = "lw_002",
            name = "Adv. Rohan Mehta",
            specialization = "Family & Divorce",
            experienceYears = 9,
            rating = 4.6f,
            location = "Mumbai",
            languages = listOf("English", "Hindi", "Marathi"),
            feePerSession = 1200,
            isOnline = true,
            bio = "Handles divorce, custody and matrimonial disputes with a focus on mediation."
        ),
        Lawyer(
            id = "lw_003",
            name = "Adv. Ananya Iyer",
            specialization = "Property & Real Estate",
            experienceYears = 15,
            rating = 4.9f,
            location = "Bengaluru",
            languages = listOf("English", "Kannada", "Tamil"),
            feePerSession = 2000,
            isOnline = false,
            bio = "RERA expert, advises on property disputes, registration and builder claims."
        ),
        Lawyer(
            id = "lw_004",
            name = "Adv. Karan Verma",
            specialization = "Corporate & Contracts",
            experienceYears = 8,
            rating = 4.5f,
            location = "Gurugram",
            languages = listOf("English", "Hindi"),
            feePerSession = 2500,
            isOnline = true,
            bio = "Advises startups and SMEs on contracts, compliance and shareholder agreements."
        ),
        Lawyer(
            id = "lw_005",
            name = "Adv. Meera Nair",
            specialization = "Consumer Rights",
            experienceYears = 6,
            rating = 4.4f,
            location = "Kochi",
            languages = listOf("English", "Malayalam"),
            feePerSession = 800,
            isOnline = true,
            bio = "Files and contests consumer complaints, refunds and service deficiency cases."
        ),
        Lawyer(
            id = "lw_006",
            name = "Adv. Siddharth Roy",
            specialization = "Cyber Law",
            experienceYears = 7,
            rating = 4.7f,
            location = "Kolkata",
            languages = listOf("English", "Bengali", "Hindi"),
            feePerSession = 1800,
            isOnline = false,
            bio = "Handles cybercrime, online fraud, data privacy and IT Act matters."
        ),
        Lawyer(
            id = "lw_007",
            name = "Adv. Neha Kapoor",
            specialization = "Labour & Employment",
            experienceYears = 10,
            rating = 4.6f,
            location = "Chandigarh",
            languages = listOf("English", "Hindi", "Punjabi"),
            feePerSession = 1000,
            isOnline = true,
            bio = "Advises on wrongful termination, PF, gratuity and workplace harassment."
        ),
        Lawyer(
            id = "lw_008",
            name = "Adv. Vikram Singh",
            specialization = "Tax Law",
            experienceYears = 14,
            rating = 4.8f,
            location = "Jaipur",
            languages = listOf("English", "Hindi"),
            feePerSession = 2200,
            isOnline = false,
            bio = "Income-tax, GST and tax dispute resolution. Appears before ITAT."
        ),
        Lawyer(
            id = "lw_009",
            name = "Adv. Fatima Khan",
            specialization = "Women & Child Rights",
            experienceYears = 11,
            rating = 4.9f,
            location = "Hyderabad",
            languages = listOf("English", "Hindi", "Urdu", "Telugu"),
            feePerSession = 900,
            isOnline = true,
            bio = "Domestic violence, POCSO and maintenance cases. Empathetic counselling."
        ),
        Lawyer(
            id = "lw_010",
            name = "Adv. Arjun Pillai",
            specialization = "Civil Litigation",
            experienceYears = 13,
            rating = 4.5f,
            location = "Chennai",
            languages = listOf("English", "Tamil"),
            feePerSession = 1600,
            isOnline = true,
            bio = "Civil suits, recovery, injunctions and appeals in the Madras High Court."
        ),
        Lawyer(
            id = "lw_011",
            name = "Adv. Pooja Deshmukh",
            specialization = "Intellectual Property",
            experienceYears = 9,
            rating = 4.7f,
            location = "Pune",
            languages = listOf("English", "Marathi", "Hindi"),
            feePerSession = 2100,
            isOnline = false,
            bio = "Trademarks, copyrights, patent prosecution and IP infringement."
        ),
        Lawyer(
            id = "lw_012",
            name = "Adv. Imran Sheikh",
            specialization = "Motor Accident Claims",
            experienceYears = 8,
            rating = 4.3f,
            location = "Ahmedabad",
            languages = listOf("English", "Hindi", "Gujarati"),
            feePerSession = 700,
            isOnline = true,
            bio = "MACT claims, insurance disputes and compensation matters."
        )
    )

    fun findById(id: String): Lawyer? = lawyers.firstOrNull { it.id == id }
}

