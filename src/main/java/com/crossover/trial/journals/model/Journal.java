package com.crossover.trial.journals.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Journal {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
	private Date publishDate;

	@ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id")
	private Publisher publisher;

    @Column(nullable = false)
    private String uuid; //external id

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    void onPersist() {
        this.publishDate = new Date();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Date getPublishDate() {
        return publishDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}