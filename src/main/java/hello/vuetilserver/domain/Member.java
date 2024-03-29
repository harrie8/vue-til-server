package hello.vuetilserver.domain;

import hello.vuetilserver.domain.dto.MemberDto;
import hello.vuetilserver.domain.dto.MemberLoginDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy", orphanRemoval = true, cascade = ALL)
    private List<Posts> postsList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "friendingMemberId", orphanRemoval = true, cascade = ALL)
    private List<Friends> friendsList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "senderId", orphanRemoval = true, cascade = ALL)
    private List<Messages> messagesList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<ChatRoomsEnter> chatRoomsEnterList = new ArrayList<>();

    public Member(MemberDto memberDto) {
        this.username = memberDto.getUsername();
        this.password = memberDto.getPassword();
        this.nickname = memberDto.getNickname();
        this.role = Role.USER;
    }

    public Member(MemberLoginDto memberLoginDto) {
        this.username = memberLoginDto.getUsername();
        this.password = memberLoginDto.getPassword();
    }

    public void update(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    //==연관관계 메서드==//
    public void addFriend(Friends friends) {
        this.friendsList.add(friends);
        friends.setFriendingMember(this);
    }

    public void addMessages(Messages messages) {
        this.messagesList.add(messages);
        messages.setSenderId(this);
    }

    public void setChatRoomsEnter(ChatRoomsEnter chatRoomsEnter) {
        this.chatRoomsEnterList.add(chatRoomsEnter);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
