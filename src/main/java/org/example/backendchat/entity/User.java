package org.example.backendchat.entity;

import org.example.backendchat.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;

	@Column(nullable = false)
	private String loginType;

	@Column(nullable = false)
	private String oauthId;

	@Column(nullable = false, unique = true)
	private String nickName;

	@Column(nullable = false)
	private String ageRange;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String nationality;

	@Column(nullable = true)
	private String selfIntroduction;

	@Column(nullable = false, unique = true)
	private String phoneNumber;

	@Column(nullable = true)
	private String userImageUrl;

	@Column(nullable = false)
	private double userScore;

}
