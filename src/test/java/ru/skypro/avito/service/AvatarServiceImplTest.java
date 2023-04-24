package ru.skypro.avito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.avito.model.Avatar;
import ru.skypro.avito.repository.AvatarRepository;
import ru.skypro.avito.service.impl.AvatarServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceImplTest {

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private AvatarServiceImpl avatarService;

    @Test
    public void testRemove() {
        Avatar avatar = new Avatar();
        avatar.setId(1);
        avatar.setData("avatar".getBytes());

        doNothing().when(avatarRepository).delete(avatar);

        avatarService.remove(avatar);

        verify(avatarRepository, only()).delete(avatar);
        verifyNoMoreInteractions(avatarRepository);
    }

}

