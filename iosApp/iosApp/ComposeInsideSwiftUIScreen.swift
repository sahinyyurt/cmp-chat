import SwiftUI

struct ComposeInsideSwiftUIScreen: View {
    var body: some View {
        ZStack {
            ComposeLayer()
            TextInputLayer()
        }.onTapGesture {
            // Hide keyboard on tap outside of TextField
            UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
        }
    }
}

struct ComposeLayer: View {
    var body: some View {
        NavigationView {
            ComposeViewControllerToSwiftUI()
                .ignoresSafeArea() // Extend Compose background behind nav title and tab bar; Compose handles its own keyboard
                .navigationTitle("The Composers Chat")
                .navigationBarTitleDisplayMode(.inline)
        }
    }
}

struct TextInputLayer: View {
    @State private var textState: String = ""
    @FocusState private var textFieldFocused: Bool

    var body: some View {
        VStack {
            Spacer()
            HStack {
                TextField("Type message…", text: $textState, axis: .vertical)
                    .focused($textFieldFocused)
                    .lineLimit(3)
                    // E2E: mirror the Compose testTag so one Maestro `id:` selects
                    // this native SwiftUI input on iOS (D3 · issue #13). Same id as
                    // SendMessage.kt's Modifier.testTag("chat_message_input").
                    .accessibilityIdentifier("chat_message_input")
                if (!textState.isEmpty) {
                    Button(action: {
                        sendMessage(textState)
                        textFieldFocused = false
                        textState = ""
                    }) {
                        Image(systemName: "arrow.up.circle.fill")
                            .tint(Color(red: 0.671, green: 0.365, blue: 0.792))
                    }
                    // E2E: the send button is an unlabeled SF-Symbol image, so it has
                    // no intrinsic selector — mirror the Compose testTag so Maestro can
                    // tap it on iOS (D3 · issue #13). Same id as SendMessage.kt's
                    // Modifier.testTag("chat_send_button").
                    .accessibilityIdentifier("chat_send_button")
                }
            }.padding(15).background(RoundedRectangle(cornerRadius: 200).fill(.white).opacity(0.95)).padding(15)
        }
    }
}
