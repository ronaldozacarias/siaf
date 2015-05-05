package ufc.quixada.npi.afastamento.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import ufc.quixada.npi.afastamento.util.Constants;

public class SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean usuarioValido = false;
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(Constants.AFFILIATION_DOCENTE) || grantedAuthority.getAuthority().equals(Constants.AFFILIATION_ADMIN_SIAF)) {
            	usuarioValido = true;
                break;
            }
        }
        if(!usuarioValido) {
        	redirectStrategy.sendRedirect(request, response, "/loginfailed");
        }
        redirectStrategy.sendRedirect(request, response, "/");
	}

}
