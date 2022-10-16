package edunhnil.project.forum.api.service.fileService;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.accessabilityRepository.Accessability;
import edunhnil.project.forum.api.dao.accessabilityRepository.AccessabilityRepository;
import edunhnil.project.forum.api.dao.fileRepository.File;
import edunhnil.project.forum.api.dao.fileRepository.FileRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.DateFormat;
import edunhnil.project.forum.api.utils.FileUtils;

@Service
public class FileServiceImpl extends AbstractService<FileRepository> implements FileService {

        @Autowired
        private FileUtils fileUtils;

        @Autowired
        private AccessabilityRepository accessabilityRepository;

        @Autowired
        private PostRepository postRepository;

        @Override
        public void createFile(FileRequest fileRequest, String loginId) {
                validate(fileRequest);
                List<Post> posts = postRepository
                                .getPostsByAuthorId(
                                                Map.ofEntries(entry("id",
                                                                generateValueForQuery(fileRequest.getBelongId())),
                                                                entry("deleted", "0"),
                                                                entry("authorId", loginId)),
                                                "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Invalid belong id!");
                }
                File file = new File(new ObjectId(), loginId, fileRequest.getTypeFile(), fileRequest.getLinkFile(),
                                DateFormat.getCurrentTime(),
                                posts.stream().map(post -> post.getId()).collect(Collectors.toList()),
                                0);
                accessabilityRepository
                                .addNewAccessability(new Accessability(null, new ObjectId(loginId),
                                                file.get_id().toString()));
                repository.saveFile(file);
        }

        @Override
        public Optional<ListWrapperResponse<FileResponse>> getFilesByUserId(
                        int page,
                        int pageSize,
                        Map<String, String> allParams,
                        String keySort,
                        String sortField,
                        String loginId,
                        boolean skipAccessability) {
                if (loginId.compareTo("public") == 0) {
                        allParams.put("deleted", "0");
                }
                List<File> files = repository.getFiles(allParams, page, pageSize, keySort, sortField).get();
                return Optional.of(new ListWrapperResponse<FileResponse>(
                                files
                                                .stream()
                                                .map(file -> fileUtils.generateFileResponse(file,
                                                                isPublic(file.getUserId(), loginId, skipAccessability)))
                                                .collect(Collectors.toList()),
                                page,
                                pageSize,
                                repository.getTotal(allParams)));
        }

        @Override
        public Optional<FileResponse> getFileById(String fileId, String loginId, boolean skipAccessability) {
                Map<String, String> allParams = Map.ofEntries(entry("_id", fileId));
                List<File> files = repository.getFiles(allParams, 0, 0, "", "").get();
                if (files.size() == 0) {
                        throw new ResourceNotFoundException("This file is deleted or not added!");
                }
                FileResponse result = fileUtils.generateFileResponse(files.get(0),
                                isPublic(files.get(0).getUserId(), loginId, skipAccessability));
                return Optional.of(result);
        }

        @Override
        public void deleteFile(String _id, String loginId, boolean skipAccessability) {
                checkAccessability(loginId, _id, skipAccessability);
                File file = repository.getFileById(_id)
                                .orElseThrow(() -> new ResourceNotFoundException("Not found file with id: " + _id));
                file.setDeleted(1);
                repository.saveFile(file);
        }

        private String generateValueForQuery(List<String> belongIds) {
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < belongIds.size(); i++) {
                        params.append(belongIds.get(i));
                        if (i != belongIds.size() - 1) {
                                params.append(",");
                        }
                }
                return params.toString();
        }
}
