package com.example.demo.chatroom.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.chatroom.GroupChat;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long>{
    @Query("SELECT gc.id AS id, gc.name AS name, gc.description AS description, " +
    "('id', p.id, 'name', p.name, 'username', p.username, 'email', p.email) AS user, " +
    "m.profilePicture AS profilePicture " +
    "FROM GroupChat gc " +
    "JOIN gc.members m " +
    "JOIN m.user p " +
    "WHERE gc.id IN (SELECT g.id FROM GroupChat g JOIN g.members mem WHERE mem.id = :profileId)")
Optional<List<Map<String, Object>>> findByMemberId(@Param("profileId") Long profileId);




    @Query("SELECT gc.id as groupId, gc.name as groupName FROM GroupChat gc LEFT JOIN gc.members c WHERE c.id = :profileId")
    Optional<List<Map<String, Object>>> findProfileSummaryByMemberId(@Param("profileId") Long profileId);

    @Query("SELECT m.id as id, m.user.username as username, m.user.name as name FROM GroupChat gc JOIN gc.members m WHERE m.id = :profileId")
    List<Map<String, Object>> findProfileSummaryByMemberId2(@Param("profileId") Long profileId);

    @Query("SELECT DISTINCT l FROM GroupChat l WHERE  l.id = :groupChatId")
    Optional<GroupChat> findGroupChatIdById(@Param("groupChatId") Long groupChatId);
}
