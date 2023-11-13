package tech.vermorken.multidbconfig.query.projection

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class BoringEntity(@Id val id: String? = null)