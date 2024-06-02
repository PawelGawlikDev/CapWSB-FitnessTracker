package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final UserServiceImpl userService;
    private final TrainingServiceImpl trainingService;
    private  final TrainingMapper trainingMapper;

    @GetMapping("all")
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("{userId}")
    public List<TrainingDto> getUserTrainings(@PathVariable long userId) {
        return trainingService.getAllTrainingsForUser(userId).stream().map(trainingMapper::toDto).toList();
    }

    @PostMapping("add_training")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto  addTraining(@RequestBody TrainingWithoutUser trainingDto) {
        long userId = trainingDto.userId();
        User user = userService.getUser(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Training training = trainingService.addTraining(trainingMapper.toEntity(trainingDto, user, null));
        return trainingMapper.toDto(training);
    }

    @GetMapping("finish_date/{finishedDate}")
    public List<TrainingDto> getAllFinishedTrainings(@PathVariable("finishedDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return trainingService.getAllFinishTrainings(date).stream().map(trainingMapper::toDto).toList();
    }

    @PutMapping("update_activity/{id}")
    public TrainingDto updateTraining(@PathVariable Long id, @RequestBody UpdateTrainingDto trainingDto) {
        Training originalTraining = trainingService.getTraining(id);

        Training training = trainingService.updateTraining(trainingMapper.toUpdateTraining(trainingDto, originalTraining.getUser(), id));
        return trainingMapper.toDto(training);
    }

    @GetMapping("get_activity")
    public List<TrainingDto> getTrainingsByType(@RequestParam String activityType) {
        ActivityType type = ActivityType.valueOf(activityType);
        return trainingService.getAllTrainingTypes(type)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }
}
