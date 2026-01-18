package com.huaxia.atlas.domain.message;

import com.huaxia.atlas.domain.message.dto.ContactForm;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    // Public: submit contact form
    @Transactional
    public Message create(ContactForm form) {
        Message m = new Message();
        m.setName(form.getName().trim());
        m.setEmail(form.getEmail().trim());
        m.setSubject(blankToNull(form.getSubject()));
        m.setMessage(form.getMessage().trim());
        m.setRead(false);
        return repo.save(m);
    }

    // Admin: list messages (optionally unread/read)
    public Page<Message> listByReadStatus(Boolean isRead, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        if (isRead == null) {
            // all messages
            return repo.findAll(pageable);
        }
        return repo.findByIsReadOrderByCreatedAtDesc(isRead, pageable);
    }

    public Optional<Message> get(Long id) {
        return repo.findById(id);
    }

    public long countUnread() {
        return repo.countByIsRead(false);
    }

    // Admin: mark read/unread
    @Transactional
    public Optional<Message> setRead(Long id, boolean read) {
        return repo.findById(id).map(m -> {
            m.setRead(read);
            return repo.save(m);
        });
    }

    private String blankToNull(String s) {
        if (s == null)
            return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
