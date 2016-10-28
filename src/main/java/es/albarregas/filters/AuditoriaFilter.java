package es.albarregas.filters;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "AuditoriaFilter", urlPatterns = {"/*"})
public class AuditoriaFilter implements Filter {

    /**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException {
		
		long antes = 0L;
		long despues = 0L;

		antes = Calendar.getInstance().getTimeInMillis();
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		despues = Calendar.getInstance().getTimeInMillis();
		
//		registrar(antes, despues, request);
			
	}

	private void registrar(long antes, long despues, ServletRequest request) 
			throws SecurityException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		FileHandler fh = new FileHandler("/Users/Jesus/logTomcat/app%g.log", 10485760, 3,true);
		fh.setFormatter(new XMLFormatter());
		Logger.getGlobal().addHandler(fh);
		Logger.getGlobal().getHandlers()[0].setLevel(Level.ALL);
		Logger.getGlobal().setLevel(Level.INFO);
		Logger.getGlobal().setUseParentHandlers(false);
		Logger.getGlobal().info("Consumidos "+(despues-antes) + 
				" msg en acceder a http://" + httpRequest.getServerName()
				+":"+httpRequest.getServerPort()+httpRequest.getRequestURI());

		
		
	}
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
