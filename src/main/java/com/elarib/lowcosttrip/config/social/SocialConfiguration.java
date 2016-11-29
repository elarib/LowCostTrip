package com.elarib.lowcosttrip.config.social;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
// jhipster-needle-add-social-connection-factory-import-package

import com.elarib.lowcosttrip.repository.CustomSocialUsersConnectionRepository;
import com.elarib.lowcosttrip.repository.SocialUserConnectionRepository;
import com.elarib.lowcosttrip.security.social.CustomSignInAdapter;


/**
 * Basic Spring Social configuration.
 *
 * <p>Creates the beans necessary to manage Connections to social services and
 * link accounts from those services to internal Users.</p>
 */
@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {
    private final Logger log = LoggerFactory.getLogger(SocialConfiguration.class);

    @Inject
    private SocialUserConnectionRepository socialUserConnectionRepository;
    
    @PostConstruct
    private void init() {
        try {
            String[] fieldsToMap = {
                "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education", "email", "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed", "install_type","is_verified", "languages", "last_name", "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format","political", "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings", "significant_other","sports", "test_group", "timezone", "third_party_id", "updated_time", "verified", "viewer_can_send_gift","website", "work"
            };

            java.lang.reflect.Field field = Class.forName("org.springframework.social.facebook.api.UserOperations").
                    getDeclaredField("PROFILE_FIELDS");
            field.setAccessible(true);

            java.lang.reflect.Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            field.set(null, fieldsToMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        // Google configuration
        String googleClientId = environment.getProperty("spring.social.google.clientId");
        String googleClientSecret = environment.getProperty("spring.social.google.clientSecret");
        if (googleClientId != null && googleClientSecret != null) {
            log.debug("Configuring GoogleConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new GoogleConnectionFactory(
                    googleClientId,
                    googleClientSecret
                )
            );
        } else {
            log.error("Cannot configure GoogleConnectionFactory id or secret null");
        }

        // Facebook configuration
        String facebookClientId = environment.getProperty("spring.social.facebook.clientId");
        String facebookClientSecret = environment.getProperty("spring.social.facebook.clientSecret");
        if (facebookClientId != null && facebookClientSecret != null) {
            log.debug("Configuring FacebookConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new FacebookConnectionFactory(
                    facebookClientId,
                    facebookClientSecret
                )
            );
        } else {
            log.error("Cannot configure FacebookConnectionFactory id or secret null");
        }

        // Twitter configuration
        String twitterClientId = environment.getProperty("spring.social.twitter.clientId");
        String twitterClientSecret = environment.getProperty("spring.social.twitter.clientSecret");
        if (twitterClientId != null && twitterClientSecret != null) {
            log.debug("Configuring TwitterConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new TwitterConnectionFactory(
                    twitterClientId,
                    twitterClientSecret
                )
            );
        } else {
            log.error("Cannot configure TwitterConnectionFactory id or secret null");
        }

        // jhipster-needle-add-social-connection-factory
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new CustomSocialUsersConnectionRepository(socialUserConnectionRepository, connectionFactoryLocator);
    }

    @Bean
    public SignInAdapter signInAdapter() {
        return new CustomSignInAdapter();
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) throws Exception {
        ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
        providerSignInController.setSignUpUrl("/social/signup");
        return providerSignInController;
    }

    @Bean
    public ProviderSignInUtils getProviderSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
    }
}
