package br.com.newtonpaiva.pi5receiveCloud.service

import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import io.jsonwebtoken.*

@Service
class ValidateJwt {

    private var jwtSecret = "MySecret"

    fun getJwtFromRequest(request: HttpServletRequest): String {
        val bearerToken : String? = request.getHeader("Authorization")
        var token = ""
        bearerToken?.let { bearerToken ->
            if (bearerToken.isNotBlank() && bearerToken.startsWith("Bearer ")) {
                token = bearerToken.substring(7, bearerToken.length)
            }}
        return token
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            throw SignatureException("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            throw MalformedJwtException("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            throw Exception("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            throw UnsupportedJwtException("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            throw IllegalArgumentException("JWT claims string is empty.")
        }
    }

}