package com.navastud.polls.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.navastud.polls.service.SpringSecurityAuditAwareImpl;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

	@Bean
	public AuditorAware<Long> auditorProvider() {
		return new SpringSecurityAuditAwareImpl();
	}

}

