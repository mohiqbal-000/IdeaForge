package com.aistartup.incubator.service;

import com.aistartup.incubator.dto.StartupRequest;
import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.model.User;
import com.aistartup.incubator.repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final StartupRepository startupRepository;

    public Startup create(User user, StartupRequest request) {
        Startup startup = Startup.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .industry(request.getIndustry())
                .build();
        return startupRepository.save(startup);
    }

    public List<Startup> listForUser(User user) {
        return startupRepository.findByUserId(user.getId());
    }

    public Startup getById(Long id) {
        return startupRepository.findById(id)
                .orElseThrow(() -> new ApiException("Startup not found", HttpStatus.NOT_FOUND));
    }
}
