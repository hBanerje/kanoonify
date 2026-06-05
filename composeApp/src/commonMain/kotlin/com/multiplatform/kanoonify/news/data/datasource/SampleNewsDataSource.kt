package com.multiplatform.kanoonify.news.data.datasource

import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import com.multiplatform.kanoonify.utils.SystemClock

/**
 * Bundled, always-available news source. Used as the offline / no-network
 * fallback so the feed is never empty during development, demos or air-gapped
 * deployments. Production data still flows through [RemoteNewsDataSource]
 * when reachable.
 */
class SampleNewsDataSource : NewsDataSource {

    override suspend fun fetchLatestNews(): List<NewsArticle> = sample()

    override suspend fun fetchCategoryNews(category: NewsCategory): List<NewsArticle> =
        if (category == NewsCategory.Latest) sample()
        else sample().filter { it.category == category }
            .ifEmpty { sample().take(3).map { it.copy(category = category) } }

    override suspend fun searchNews(query: String): List<NewsArticle> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()
        return sample().filter {
            it.title.contains(q, ignoreCase = true) ||
                it.description.contains(q, ignoreCase = true) ||
                it.source.contains(q, ignoreCase = true)
        }
    }

    override suspend fun fetchArticle(articleId: String): NewsArticle? =
        sample().firstOrNull { it.id == articleId }

    /* ------------------------------ seed ----------------------------------- */

    private fun sample(): List<NewsArticle> {
        val now = SystemClock.currentTimeMillis()
        fun mins(n: Long) = now - n * 60_000L
        return listOf(
            NewsArticle(
                id = "sample-1",
                title = "Supreme Court expands the right to privacy in landmark ruling",
                description = "A nine-judge bench unanimously holds that the right to privacy is a fundamental right under Article 21 of the Constitution.",
                content = "In a far-reaching judgment, the Supreme Court ruled that the right to privacy forms an intrinsic part of the right to life and personal liberty guaranteed under Article 21. The decision has wide implications across data protection, surveillance and consent-based governance.",
                imageUrl = "",
                source = "Bar & Bench",
                author = "Editorial Desk",
                publishedAtEpochMs = mins(45),
                category = NewsCategory.Law,
                articleUrl = "https://example.com/news/privacy-article-21"
            ),
            NewsArticle(
                id = "sample-2",
                title = "Parliament passes the Digital India Act, replacing the IT Act",
                description = "The new framework introduces a tiered regulatory model for online platforms, AI systems and intermediaries.",
                content = "After weeks of debate, both houses passed the Digital India Act with bipartisan support. The Act creates a unified compliance framework for online platforms, mandates explicit consent for AI-driven decisioning and introduces a digital ombudsman.",
                imageUrl = "",
                source = "PIB India",
                author = "Press Bureau",
                publishedAtEpochMs = mins(120),
                category = NewsCategory.Parliament,
                articleUrl = "https://example.com/news/digital-india-act"
            ),
            NewsArticle(
                id = "sample-3",
                title = "RBI keeps repo rate unchanged; flags inflation watchfulness",
                description = "MPC votes 5-1 to hold rates, citing transient food-price shocks and stable core inflation.",
                content = "The Monetary Policy Committee chose to keep the repo rate steady at 6.5% in a 5-1 vote. Governor Shaktikanta Das emphasised a 'withdrawal of accommodation' stance as inflation hovers above the 4% target.",
                imageUrl = "",
                source = "Mint",
                author = "Macro Desk",
                publishedAtEpochMs = mins(180),
                category = NewsCategory.Finance,
                articleUrl = "https://example.com/news/rbi-mpc-hold"
            ),
            NewsArticle(
                id = "sample-4",
                title = "ISRO's Gaganyaan crew capsule clears pad-abort test",
                description = "The first uncrewed test validates the launch escape system ahead of the maiden crewed mission.",
                content = "The Indian Space Research Organisation successfully completed the pad-abort test of the Gaganyaan crew module. The mission marks a critical milestone toward India's first human spaceflight.",
                imageUrl = "",
                source = "The Hindu",
                author = "Science Bureau",
                publishedAtEpochMs = mins(240),
                category = NewsCategory.India,
                articleUrl = "https://example.com/news/gaganyaan-pad-abort"
            ),
            NewsArticle(
                id = "sample-5",
                title = "Tata Group eyes \$5B EV battery gigafactory in Gujarat",
                description = "The proposed plant would be one of the largest in Asia and supply both Tata Motors and third parties.",
                content = "Tata Sons confirmed plans for a 40 GWh battery gigafactory in Sanand, Gujarat, expected to be commissioned in phases starting 2027. The investment underscores India's push to localise EV supply chains.",
                imageUrl = "",
                source = "Economic Times",
                author = "Corporate Desk",
                publishedAtEpochMs = mins(330),
                category = NewsCategory.Corporate,
                articleUrl = "https://example.com/news/tata-gigafactory"
            ),
            NewsArticle(
                id = "sample-6",
                title = "Apple iPhone production in India crosses \$14B mark",
                description = "Foxconn, Pegatron and Wistron together exported a record volume in the last fiscal year.",
                content = "Apple's India contract manufacturers exported iPhones worth over \$14 billion in FY26, cementing India as Apple's second-largest production hub after China.",
                imageUrl = "",
                source = "Bloomberg",
                author = "Tech Desk",
                publishedAtEpochMs = mins(420),
                category = NewsCategory.Technology,
                articleUrl = "https://example.com/news/iphone-india-exports"
            ),
            NewsArticle(
                id = "sample-7",
                title = "ICC announces revamped T20 World Cup format from 2027",
                description = "Expanded to 24 teams with a new super-eights stage replacing the existing super-twelves.",
                content = "The International Cricket Council unveiled a 24-team format for the 2027 T20 World Cup, with the tournament expanding to span four weeks across multiple host nations.",
                imageUrl = "",
                source = "ESPN Cricinfo",
                author = "Sports Desk",
                publishedAtEpochMs = mins(540),
                category = NewsCategory.Sports,
                articleUrl = "https://example.com/news/t20-world-cup-format"
            ),
            NewsArticle(
                id = "sample-8",
                title = "G20 commits to global minimum corporate tax floor by 2027",
                description = "Finance ministers reaffirm the 15% floor and outline an implementation roadmap.",
                content = "G20 finance ministers, meeting in São Paulo, reaffirmed commitment to the OECD-led 15% global minimum corporate tax floor with binding implementation timelines for member states.",
                imageUrl = "",
                source = "Reuters",
                author = "Global Desk",
                publishedAtEpochMs = mins(640),
                category = NewsCategory.World,
                articleUrl = "https://example.com/news/g20-minimum-tax"
            ),
            NewsArticle(
                id = "sample-9",
                title = "Election Commission opens online voter-roll corrections nationwide",
                description = "A new portal allows real-time grievance redressal during the revision window.",
                content = "The Election Commission of India launched a unified online portal for voter roll corrections, allowing citizens to track and resolve grievances in near real time.",
                imageUrl = "",
                source = "The Indian Express",
                author = "Politics Bureau",
                publishedAtEpochMs = mins(720),
                category = NewsCategory.Politics,
                articleUrl = "https://example.com/news/eci-portal"
            ),
            NewsArticle(
                id = "sample-10",
                title = "SEBI tightens insider-trading rules for listed firms",
                description = "New rules mandate stricter disclosures and shorter window periods for designated persons.",
                content = "SEBI's amendments tighten the disclosure obligations for designated persons in listed companies, including spouses and dependent relatives, with significantly reduced trading windows.",
                imageUrl = "",
                source = "Business Standard",
                author = "Markets Desk",
                publishedAtEpochMs = mins(900),
                category = NewsCategory.Business,
                articleUrl = "https://example.com/news/sebi-insider"
            ),
            NewsArticle(
                id = "sample-11",
                title = "Delhi HC: Police cannot demand phone unlock without warrant",
                description = "Single-judge bench expands jurisprudence on digital privacy during routine stops.",
                content = "The Delhi High Court ruled that demanding a citizen to unlock their phone during routine police stops without judicial authorisation violates the right to privacy under Article 21.",
                imageUrl = "",
                source = "LiveLaw",
                author = "Legal Correspondent",
                publishedAtEpochMs = mins(1080),
                category = NewsCategory.Law,
                articleUrl = "https://example.com/news/delhi-hc-phone-unlock"
            ),
            NewsArticle(
                id = "sample-12",
                title = "Indian Cyber Crime cell flags rise in deepfake-based fraud",
                description = "I4C advisory urges KYC re-verification protocols across BFSI.",
                content = "The Indian Cyber Crime Coordination Centre (I4C) issued an advisory warning of a sharp rise in deepfake-driven impersonation scams, urging banks and NBFCs to upgrade KYC re-verification protocols.",
                imageUrl = "",
                source = "Hindustan Times",
                author = "Cyber Desk",
                publishedAtEpochMs = mins(1320),
                category = NewsCategory.Technology,
                articleUrl = "https://example.com/news/i4c-deepfake-advisory"
            )
        )
    }
}

