package com.huaxia.atlas.domain.seed;

import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingRepository;
import com.huaxia.atlas.domain.post.Post;
import com.huaxia.atlas.domain.post.PostRepository;
import com.huaxia.atlas.domain.post.PostStatus;
import com.huaxia.atlas.domain.product.Product;
import com.huaxia.atlas.domain.product.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SampleDataSeeder implements ApplicationRunner {

    private final BuildingRepository buildingRepository;
    private final PostRepository postRepository;
    private final ProductRepository productRepository;

    public SampleDataSeeder(BuildingRepository buildingRepository,
                            PostRepository postRepository,
                            ProductRepository productRepository) {
        this.buildingRepository = buildingRepository;
        this.postRepository = postRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedBuildings();
        seedPosts();
        seedProducts();
    }

    private void seedBuildings() {
        if (buildingRepository.count() > 0) {
            return;
        }

        Building b1 = new Building();
        b1.setName("Zhaozhou Bridge");
        b1.setDynasty("Sui");
        b1.setLocation("Zhao County, Hebei");
        b1.setType("Bridge");
        b1.setYearBuilt("c. 605");
        b1.setDescription("Stone segmental arch bridge; early bridge engineering milestone.");
        b1.setTags("stone arch,bridge,engineering");

        Building b2 = new Building();
        b2.setName("Forbidden City (Palace Complex)");
        b2.setDynasty("Ming");
        b2.setLocation("Beijing");
        b2.setType("Palace");
        b2.setYearBuilt("1406-1420");
        b2.setDescription("Imperial palace complex with axial planning, courtyards, and timber halls.");
        b2.setTags("palace,courtyard,timber,imperial");

        Building b3 = new Building();
        b3.setName("Siheyuan Courtyard Residence");
        b3.setDynasty("Qing");
        b3.setLocation("Beijing");
        b3.setType("Residence");
        b3.setYearBuilt("18th-19th century");
        b3.setDescription("Traditional courtyard house emphasizing symmetry and hierarchy.");
        b3.setTags("residence,courtyard,vernacular");

        Building b4 = new Building();
        b4.setName("Hutong Street Layout");
        b4.setDynasty("Ming");
        b4.setLocation("Beijing");
        b4.setType("Urban Fabric");
        b4.setYearBuilt("15th-19th century");
        b4.setDescription("Lane-based urban pattern supporting courtyard housing and neighborhood life.");
        b4.setTags("urban,streets,housing");

        Building b5 = new Building();
        b5.setName("Ancient City Wall (Representative Section)");
        b5.setDynasty("Ming");
        b5.setLocation("Xian, Shaanxi");
        b5.setType("Fortification");
        b5.setYearBuilt("14th century");
        b5.setDescription("Large-scale defensive wall system with gates and watchtowers.");
        b5.setTags("wall,fortification,masonry");

        Building b6 = new Building();
        b6.setName("Traditional Yamen (Local Government Office)");
        b6.setDynasty("Qing");
        b6.setLocation("Northern China");
        b6.setType("Office");
        b6.setYearBuilt("17th-19th century");
        b6.setDescription("Local administrative office compound with formal halls and courtyards.");
        b6.setTags("government,office,courtyard");

        Building b7 = new Building();
        b7.setName("Classical Garden Residence");
        b7.setDynasty("Ming");
        b7.setLocation("Suzhou, Jiangsu");
        b7.setType("Residence");
        b7.setYearBuilt("16th-17th century");
        b7.setDescription("Residential garden composition with pavilions, rocks, and water features.");
        b7.setTags("garden,residence,landscape");

        Building b8 = new Building();
        b8.setName("Academy Courtyard (Study Hall)");
        b8.setDynasty("Song");
        b8.setLocation("Southern China");
        b8.setType("Academy");
        b8.setYearBuilt("11th-13th century");
        b8.setDescription("Educational courtyard complex used for teaching and scholarship.");
        b8.setTags("education,academy,courtyard");

        buildingRepository.saveAll(List.of(b1, b2, b3, b4, b5, b6, b7, b8));
    }

    private void seedPosts() {
        if (postRepository.count() > 0) {
            return;
        }

        Post p1 = new Post();
        p1.setTitle("What makes timber-frame architecture durable?");
        p1.setContent("Timber structures can last centuries when protected from moisture and designed with proper joinery.");
        p1.setAuthorName("Abdullah");
        p1.setAuthorEmail("abdullah@example.com");
        p1.setStatus(PostStatus.APPROVED);

        Post p2 = new Post();
        p2.setTitle("I found a historic courtyard house in my city");
        p2.setContent("I want to share photos and a short history. Please review and approve my post.");
        p2.setAuthorName("Student User");
        p2.setAuthorEmail("student@example.com");
        p2.setStatus(PostStatus.PENDING);

        postRepository.saveAll(List.of(p1, p2));
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return;
        }

        Product p1 = new Product();
        p1.setName("Atlas Field Guide");
        p1.setDescription("Compact guide to courtyard and palace layouts.");
        p1.setPrice(new BigDecimal("29.00"));
        p1.setStock(12);

        Product p2 = new Product();
        p2.setName("Courtyard Sketch Set");
        p2.setDescription("Study sketches and site notes for historic compounds.");
        p2.setPrice(new BigDecimal("18.50"));
        p2.setStock(8);

        Product p3 = new Product();
        p3.setName("Architectural Map Print");
        p3.setDescription("Large format print of key pre-1911 sites.");
        p3.setPrice(new BigDecimal("42.00"));
        p3.setStock(5);

        productRepository.saveAll(List.of(p1, p2, p3));
    }
}
