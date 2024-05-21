package it.gov.acn.outbox.context;

import it.gov.acn.outbox.etc.Utils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.Map;


public class PostgresDatasourceRequirement implements ContextRequirement{

    private ApplicationContext applicationContext;

    public PostgresDatasourceRequirement(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Override
    public boolean isSatisfied() {
        try {
            DataSource dataSource = Utils.getBeanIfExists(DataSource.class, applicationContext);
            if (dataSource != null) {
                String url = dataSource.getConnection().getMetaData().getURL();
                return url != null && url.contains("jdbc:postgresql:");
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Requires a Postgres datasource.";
    }

    @Override
    public boolean isError() {
        return true;
    }
}
