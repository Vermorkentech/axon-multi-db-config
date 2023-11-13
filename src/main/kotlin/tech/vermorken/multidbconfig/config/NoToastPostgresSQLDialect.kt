package tech.vermorken.multidbconfig.config

import org.hibernate.boot.model.TypeContributions
import org.hibernate.dialect.DatabaseVersion
import org.hibernate.dialect.PostgreSQLDialect
import org.hibernate.service.ServiceRegistry
import org.hibernate.type.SqlTypes
import org.hibernate.type.descriptor.jdbc.BinaryJdbcType
import java.sql.Types


class NoToastPostgresSQLDialect : PostgreSQLDialect(DatabaseVersion.make(9, 5)) {
    override fun columnType(sqlTypeCode: Int): String {
        return if (sqlTypeCode == SqlTypes.BLOB) {
            "bytea"
        } else super.columnType(sqlTypeCode)
    }

    override fun castType(sqlTypeCode: Int): String {
        return if (sqlTypeCode == SqlTypes.BLOB) {
            "bytea"
        } else super.castType(sqlTypeCode)
    }

    override fun contributeTypes(typeContributions: TypeContributions, serviceRegistry: ServiceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry)
        val jdbcTypeRegistry = typeContributions.typeConfiguration.jdbcTypeRegistry
        jdbcTypeRegistry.addDescriptor(Types.BLOB, BinaryJdbcType.INSTANCE)
    }
}