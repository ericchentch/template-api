package edunhnil.project.forum.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import edunhnil.project.forum.api.dao.featureRepository.Feature;
import edunhnil.project.forum.api.dao.featureRepository.FeatureRepository;
import edunhnil.project.forum.api.dao.permissionRepository.Permission;
import edunhnil.project.forum.api.dao.permissionRepository.PermissionRepository;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.utils.DateFormat;

@Component
public class EndpointsListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    protected String email;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods()
                .forEach((key, value) -> {
                    Set<String> pathList = key.getDirectPaths();
                    for (String path : pathList) {
                        if (featureRepository.getFeatureByPath(path).isEmpty()) {
                            Feature feature = new Feature();
                            feature.setName(value.toString());
                            feature.setPath(path);
                            feature.setDeleted(0);
                            featureRepository.insertAndUpdate(feature);
                        }
                    }
                });
        if (userRepository.checkUsername("super_admin").isEmpty()) {
            User user = new User(null, "super_admin", "$2a$12$d6aWvOFKgqCVIaYJc9YkDu0y.wK8reuZXLwoUjgnNOP9YujICIHLm", 0,
                    "", "", "Super", "Admin", email, "", "",
                    DateFormat.getCurrentTime(), null, "", true, false, null, 0);
            userRepository.insertAndUpdate(user);
        }
        User user = userRepository.checkUsername("super_admin").get();
        Map<String, String> permissionParams = new HashMap<>();
        permissionParams.put("name", "super_admin_permission");
        List<Permission> permissions = permissionRepository.getPermissions(permissionParams, "", 0, 0, "").get();
        if (permissions.size() == 0) {
            List<ObjectId> features = featureRepository.getFeatures(new HashMap<>(), "", 0, 0, "").get().stream()
                    .map(feature -> feature.get_id()).collect(Collectors.toList());
            List<ObjectId> users = new ArrayList<>();
            users.add(user.get_id());
            Permission permission = new Permission(null, "super_admin_permission", features, users,
                    DateFormat.getCurrentTime(), null);
            permissionRepository.insertAndUpdate(permission);
        } else {
            List<ObjectId> features = featureRepository.getFeatures(new HashMap<>(), "", 0, 0, "").get().stream()
                    .map(feature -> feature.get_id()).collect(Collectors.toList());
            List<ObjectId> users = permissions.get(0).getUserId();
            Permission permission = new Permission(permissions.get(0).get_id(), "super_admin_permission", features,
                    users,
                    permissions.get(0).getCreated(), DateFormat.getCurrentTime());
            permissionRepository.insertAndUpdate(permission);

        }
    }

}
