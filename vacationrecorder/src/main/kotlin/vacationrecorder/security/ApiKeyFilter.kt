package vacationrecorder.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyFilter : OncePerRequestFilter() {
    @Value("\${API_KEY}")
    private val apiKey: String? = null
    @Value("\${API_KEY_VALUE}")
    private val apiKeyValue: String? = null

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(apiKey)
        if (authorizationHeader != null && authorizationHeader.equals(apiKeyValue)) {
            filterChain.doFilter(request, response)
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
    }
}