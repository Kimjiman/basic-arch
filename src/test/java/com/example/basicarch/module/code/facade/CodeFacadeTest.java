package com.example.basicarch.module.code.facade;

import com.example.basicarch.base.exception.CustomException;
import com.example.basicarch.module.code.converter.CodeConverter;
import com.example.basicarch.module.code.converter.CodeGroupConverter;
import com.example.basicarch.module.code.model.CodeGroupModel;
import com.example.basicarch.module.code.model.CodeModel;
import com.example.basicarch.module.code.service.CodeGroupCacheService;
import com.example.basicarch.module.code.service.CodeGroupService;
import com.example.basicarch.module.code.service.CodeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


/**
 * packageName    : com.example.basicarch.module.user.facade
 * fileName       : CodeFacadeTest
 * author         : KIM JIMAN
 * date           : 26. 3. 8. 일요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 8.     KIM JIMAN      First Commit
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CodeFacade 테스트")
public class CodeFacadeTest {
    @Mock
    private CodeService codeService;
    @Mock
    private CodeGroupService codeGroupService;
    @Mock
    private CodeConverter codeConverter;
    @Mock
    private CodeGroupConverter codeGroupConverter;
    @Mock
    private CodeGroupCacheService codeGroupCacheService;

    @InjectMocks
    private CodeFacade codeFacade;

    @Test
    @DisplayName("createCodeGroup 성공")
    void createCodeGroupSuc() {
        CodeGroupModel codeGroupModel = CodeGroupModel.builder()
                .id(1L)
                .codeGroup("ADM")
                .name("name")
                .build();

        codeFacade.createCodeGroup(codeGroupModel);
        verify(codeGroupService).save(any());
    }


    @Test
    @DisplayName("createCode 실패")
    void createCodeFail() {
        CodeModel codeModel = CodeModel.builder()
                .code("001")
                .name("name")
                .build();

        assertThrows(CustomException.class, () -> codeFacade.createCode(codeModel));
    }

    @Test
    @DisplayName("createCode 성공")
    void createCodeSuc() {
        CodeModel codeModel = CodeModel.builder()
                .codeGroupId(1L)
                .code("001")
                .name("name")
                .build();

        codeFacade.createCode(codeModel);
        verify(codeService).save(any());
    }


    @Test
    @DisplayName("createCodeGroup 실패")
    void createCodeGroupFail() {
        CodeGroupModel codeGroupModel = CodeGroupModel.builder()
                .id(1L)
                .name("name")
                .build();

        assertThrows(CustomException.class, () -> codeFacade.createCodeGroup(codeGroupModel));
    }

    @Test
    @DisplayName("removeCodeGroupById 테스트, id 집어넣고 성공")
    void removeCodeGroupByIdSuc() {
        CodeGroupModel codeGroupModel = CodeGroupModel.builder()
                .id(1L)
                .codeGroup("ADM")
                .build();

        codeFacade.removeCodeGroupById(codeGroupModel.getId());
        verify(codeService).deleteByCodeGroupId(codeGroupModel.getId());
        verify(codeGroupService).deleteById(codeGroupModel.getId());
    }

    @Test
    @DisplayName("removeCodeGroupById 테스트, id가 없으면 예외")
    void removeCodeGroupByIdFail() {
        CodeGroupModel codeGroupModel = CodeGroupModel.builder()
                .codeGroup("ADM")
                .build();

        assertThrows(CustomException.class, () -> codeFacade.removeCodeGroupById(codeGroupModel.getId()));
    }
}
