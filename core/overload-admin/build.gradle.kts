dependencies {
    val keycloakVersion: String by project
    implementation("org.keycloak:keycloak-admin-client:${keycloakVersion}")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
}
