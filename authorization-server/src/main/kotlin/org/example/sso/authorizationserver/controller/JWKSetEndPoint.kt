package org.example.sso.authorizationserver.controller

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JWKSetEndPoint(
    private val jwkSource: JWKSource<SecurityContext>
) {
    @GetMapping("/.well-known/jwks.json")
    fun getKey(): Any {
        val jwk =  jwkSource as ImmutableJWKSet<SecurityContext>

        // 创建只包含公钥信息的JWKSet
        val keys = jwk.jwkSet.toPublicJWKSet().keys
        val publicKeys = ArrayList<JWK>()
        for (jwk in keys) {
            // 确保只返回公钥信息
            publicKeys.add(jwk.toPublicJWK())
        }
        return JWKSet(publicKeys).toJSONObject()
    }
}