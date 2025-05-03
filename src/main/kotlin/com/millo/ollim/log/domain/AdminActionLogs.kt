package com.millo.ollim.log.domain

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.sql.SQLType
import java.util.UUID

@Entity
@Table(name = "admin_action_logs")
data class AdminActionLogs(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long,
    @Column(name = "user_id", nullable = false)
    var userId: UUID,
    @Column(name = "action_type", nullable = false)
    var actionType: String,
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "meta")
    var meta: Map<String,Any>,
    @Column(name = "occured_at", nullable = false)
    var occuredAt: String,
)
