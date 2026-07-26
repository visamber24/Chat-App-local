
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias{ ktorLibs.plugins.serialization }
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
val ktor_version = "3.5.1"
val koin_version = "4.2.2"
val kmongo_version = "5.6.0"
dependencies {
    // Our new dependency to be added
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.websockets)
    implementation(ktorLibs.server.websockets)
    implementation(ktorLibs.serialization)
    implementation(ktorLibs.server.sessions)
    implementation("ch.qos.logback:logback-classic:1.5.38")
    // KMongo
//    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.0")
//    implementation("org.litote.kmongo:kmongo-coroutine:${kmongo_version}")

    // Koin core features
    implementation("io.insert-koin:koin-core:${koin_version}")
    implementation("io.insert-koin:koin-ktor:${koin_version}")
    implementation("io.insert-koin:koin-logger-slf4j:${koin_version}")

//      Bson
    implementation("org.mongodb:bson-kotlin:5.1.0")
    // json
    implementation(ktorLibs.serialization.kotlinx.json)

    //  content negotiation
    implementation(ktorLibs.server.contentNegotiation)
    // call loging
    implementation(ktorLibs.server.callLogging)

    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
